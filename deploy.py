#!/usr/bin/env python3
"""deploy

Copy freshly built mod jars from a build output folder into the matching
PrismLauncher (MultiMC layout) instances.

If that file does not exist it is created with sensible defaults (matching this
project) and the script exits so you can tweak it first.

Usage:
    python3 ./deploy.py                # deploy everything
    python3 ./deploy.py --dry-run      # show what would happen
    python3 ./deploy.py --only 26.3-fabric
    python3 ./deploy.py --list
    python3 ./deploy.py --config other.json
"""

from __future__ import annotations

import argparse
import fnmatch
import json
import shutil
import sys
from pathlib import Path

SCRIPT = Path(__file__).resolve()
SCRIPT_DIR = SCRIPT.parent
DEFAULT_CONFIG = SCRIPT.with_suffix(".json")

DEFAULT_CONFIG_DATA = {
    # Base folder. Relative paths elsewhere resolve against this.
    "project_dir": ".",
    # Where the built jars live, relative to project_dir.
    "source_dir": "build/libs",
    # PrismLauncher instances folder.
    "instances_dir": "~/Library/Application Support/PrismLauncher/instances",
    # Layout inside an instance folder.
    "instance_game_subdir": "minecraft",
    "mods_subdir": "mods",
    # Which jar files inside each source folder are candidates...
    "include": ["*.jar"],
    # ...and which of those to ignore (sources / intermediate jars).
    "exclude": ["*-sources.jar", "*-dev-shadow.jar"],
    # Delete already-installed files matching these patterns before copying.
    "clean": ["customcursor-*"],
    # src = folder name in source_dir, instance = folder name in instances_dir.
    "mappings": [
        {"src": "26.1-fabric", "instance": "26.1.2(1)"},
        {"src": "26.1-neoforge", "instance": "26.1.2"},
        {"src": "26.2-fabric", "instance": "26.2(1)"},
        {"src": "26.2-neoforge", "instance": "26.2"},
        {"src": "26.3-fabric", "instance": "26.3"},
        {"src": "26.3-neoforge", "instance": "26.3(1)"},
    ],
}


def load_config(path: Path) -> dict:
    if not path.exists():
        path.write_text(json.dumps(DEFAULT_CONFIG_DATA, indent=2) + "\n")
        print(f"Created default config: {path}")
        print("Edit it, then run the script again.")
        sys.exit(0)

    try:
        data = json.loads(path.read_text())
    except json.JSONDecodeError as exc:
        sys.exit(f"Invalid JSON in {path}: {exc}")

    # Merge so a partial config still works.
    config = dict(DEFAULT_CONFIG_DATA)
    config.update(data)
    return config


def matches_any(name: str, patterns: list[str]) -> bool:
    return any(fnmatch.fnmatch(name, p) for p in patterns)


def resolve_jar(directory: Path, include: list[str], exclude: list[str],
                per_map_exclude: list[str]) -> list[Path]:
    if not directory.is_dir():
        return []

    jars: list[Path] = []
    for entry in sorted(directory.iterdir()):
        if not entry.is_file():
            if matches_any(entry.name, include):
                print(f"  ! skipping non-file (extracted jar?): {entry.name}")
            continue
        if not matches_any(entry.name, include):
            continue
        if matches_any(entry.name, exclude) or matches_any(entry.name, per_map_exclude):
            continue
        jars.append(entry)
    return jars


def main() -> int:
    parser = argparse.ArgumentParser(
        description="Deploy built mod jars into PrismLauncher instances.",
    )
    parser.add_argument("--config", type=Path, default=DEFAULT_CONFIG,
                        help=f"config file (default: {DEFAULT_CONFIG.name})")
    parser.add_argument("--dry-run", action="store_true",
                        help="print actions without touching anything")
    parser.add_argument("--only", action="append", metavar="SRC",
                        help="only deploy this src folder (repeatable)")
    parser.add_argument("--list", action="store_true",
                        help="list mappings and resolved jars, then exit")
    args = parser.parse_args()

    config = load_config(args.config.resolve())

    config_dir = args.config.resolve().parent
    project_dir = (config_dir / config["project_dir"]).resolve()
    source_dir = (project_dir / config["source_dir"]).resolve()
    instances_dir = Path(config["instances_dir"]).expanduser().resolve()
    game_sub = config["instance_game_subdir"]
    mods_sub = config["mods_subdir"]
    include = config["include"]
    exclude = config["exclude"]
    clean = config["clean"]

    if not instances_dir.is_dir():
        sys.exit(f"Instances folder not found: {instances_dir}")

    mappings = config["mappings"]
    if args.only:
        wanted = set(args.only)
        mappings = [m for m in mappings if m["src"] in wanted]
        missing = wanted - {m["src"] for m in mappings}
        if missing:
            print(f"warning: no mapping for {', '.join(sorted(missing))}")

    print(f"source:    {source_dir}")
    print(f"instances: {instances_dir}")
    print()

    deployed = 0
    for mapping in mappings:
        src_name = mapping["src"]
        instance_name = mapping["instance"]
        src_path = source_dir / src_name
        mods_path = instances_dir / instance_name / game_sub / mods_sub
        per_map_exclude = mapping.get("exclude", [])

        print(f"[{src_name}] -> {instance_name}")

        jars = resolve_jar(src_path, include, exclude, per_map_exclude)
        if not jars:
            print(f"  ! no deployable jar in {src_path}")
            continue

        if not mods_path.is_dir():
            if args.dry_run:
                print(f"  would create {mods_path}")
            else:
                mods_path.mkdir(parents=True, exist_ok=True)
                print(f"  created {mods_path}")

        # Old installs that would be cleaned out.
        stale: list[Path] = []
        if mods_path.is_dir():
            stale = [p for p in sorted(mods_path.iterdir())
                     if p.is_file() and matches_any(p.name, clean)]

        for existing in stale:
            print(f"  rm   {existing.name}")
        for jar in jars:
            print(f"  copy {jar.name}")

        if args.list or args.dry_run:
            continue

        for existing in stale:
            existing.unlink()

        for jar in jars:
            shutil.copy2(jar, mods_path / jar.name)
            deployed += 1

    print()
    if args.list:
        print("(list only, nothing copied)")
    elif args.dry_run:
        print("dry run, nothing copied")
    else:
        print(f"deployed {deployed} jar(s)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
