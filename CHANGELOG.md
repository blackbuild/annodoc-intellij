<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# AnnoDoc Support for IntelliJ IDEA Changelog

## [Unreleased]
### Added
- Quick Documentation fallback for AnnoDoc metadata on compiled Java types, methods, constructors, and fields without attached sources.
- Javadoc markup and representative block-tag rendering through IntelliJ's documentation UI.
- IntelliJ fixture coverage using the real AnnoDocimal annotation artifact, including native source/external-Javadoc precedence and missing, blank, malformed, unsupported, or absent annotation content.

### Changed
- Rebuilt the plugin scaffold for Java 21 and IntelliJ IDEA 2025.3+, with the bundled Java plugin as the only runtime platform dependency.
- Removed the abandoned Kotlin template services, startup activity, and tool window.
