#!/usr/bin/env bash
set -euo pipefail

repository_root=$(cd "$(dirname "$0")/../.." && pwd)
artifact_script="$repository_root/.github/scripts/prepare-plugin-artifact.sh"
test_directory=$(mktemp -d)
trap 'rm -rf -- "$test_directory"' EXIT

output_file="$test_directory/github-output"
distribution_directory="$test_directory/distributions"
mkdir "$distribution_directory"

if GITHUB_OUTPUT="$output_file" bash "$artifact_script" "$distribution_directory"; then
  echo "Expected artifact preparation to reject an empty distribution directory." >&2
  exit 1
fi

archive_path="$distribution_directory/annodoc-intellij-0.1.0-alpha.1.zip"
touch "$archive_path"
GITHUB_OUTPUT="$output_file" bash "$artifact_script" "$distribution_directory"
expected_output=$'filename=annodoc-intellij-0.1.0-alpha.1.zip\npath='"$archive_path"
if [[ $(<"$output_file") != "$expected_output" ]]; then
  echo "Artifact preparation did not write the expected GitHub Actions outputs." >&2
  exit 1
fi

touch "$distribution_directory/second.zip"
if GITHUB_OUTPUT="$output_file" bash "$artifact_script" "$distribution_directory"; then
  echo "Expected artifact preparation to reject multiple plugin ZIPs." >&2
  exit 1
fi

newline_directory="$test_directory/newline-distribution"
mkdir "$newline_directory"
newline_archive_name=$'annodoc\nintellij.zip'
touch "$newline_directory/$newline_archive_name"
if GITHUB_OUTPUT="$output_file" bash "$artifact_script" "$newline_directory"; then
  echo "Expected artifact preparation to reject a ZIP path containing a newline." >&2
  exit 1
fi
