<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# AnnoDoc Support for IntelliJ IDEA Changelog

## Unreleased

- Adopt Blackbuild's permanent AnnoDoc Support identity and repository coordinates.

## [Unreleased]
### Added
- Quick Documentation fallback for AnnoDoc metadata on compiled Java types, methods, constructors, and fields without attached sources.
- Javadoc markup and representative block-tag rendering through IntelliJ's documentation UI.
- IntelliJ fixture coverage using the real AnnoDocimal annotation artifact, including native source/external-Javadoc precedence and missing, blank, malformed, unsupported, or absent annotation content.
- Compatibility coverage for Quick Documentation on a real KlumAST-generated method from explicit-builder and factory-closure Groovy call sites.

### Changed
- Rebuilt the plugin scaffold for Java 21 and IntelliJ IDEA 2025.3+, with the bundled Java plugin as the only runtime platform dependency.
- Removed the abandoned Kotlin template services, startup activity, and tool window.
