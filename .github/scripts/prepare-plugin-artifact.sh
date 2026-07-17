#!/usr/bin/env bash
set -euo pipefail

if (( $# != 1 )); then
  echo "Usage: $0 <distribution-directory>" >&2
  exit 2
fi

distribution_directory=$1
if [[ ! -d "$distribution_directory" ]]; then
  echo "Plugin distribution directory does not exist: $distribution_directory" >&2
  exit 1
fi

mapfile -d '' -t archives < <(
  find "$distribution_directory" -maxdepth 1 -type f -name '*.zip' -print0 | LC_ALL=C sort -z
)

if (( ${#archives[@]} != 1 )); then
  echo "Expected exactly one plugin ZIP in $distribution_directory, found ${#archives[@]}." >&2
  exit 1
fi

archive_path=${archives[0]}
archive_name=${archive_path##*/}
if [[ "$archive_path" == *$'\n'* || "$archive_path" == *$'\r'* || "$archive_name" == *$'\n'* || "$archive_name" == *$'\r'* ]]; then
  echo "Plugin artifact name and path must not contain a newline." >&2
  exit 1
fi

: "${GITHUB_OUTPUT:?GITHUB_OUTPUT must be set by GitHub Actions}"
printf 'filename=%s\npath=%s\n' "$archive_name" "$archive_path" >> "$GITHUB_OUTPUT"
