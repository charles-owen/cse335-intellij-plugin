<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# cse335-intellij-plugin Changelog

## [Unreleased]

## [1.0.5]
- Updating to CLion 2022.2
- Fixed bug when connecting to /ide/ on server

## [1.0.4]
- Skipped version

## [1.0.3]
- Added individualized plugin code

## [1.0.2]
- Updating to CLion 2021.3.1
- Login now checks to ensure the version is up to date
- Now ensures pch.h is the first header included
- Allows for quoted headers in testing projects if in a directory with "help" in the name
- Tests for incorrect membership in headers (Game::Game())
- Changelog is not working, so manually added changes to plugin.xml

## [0.4.2]
- Fixed problems with paths when exporting on a Windows system
- Fixed issues with use of FileSaverDescriptor and zip files
- Sanity check bug fixed that showed cycles where there were none
- Fixed how the Sanity check window is brought up
- Hopefully understand how the changelog works

## [0.2.0]
- Sanity check added test for pch.h include
- Sanity check added test for quoted includes in Tests
- Sanity check added test for include cycles
- Changed configurations to Java 11

## [0.1.0] - 2021-08-09
- Export of CLion files without the cmake- and .idea directories
- Login to the CSE335 course website
- Submission of assignments to the CSE335 course website
- Sanity check system
- Checks for using namespace misuse
- Checks for missing file header information
- CSE335 logo icon

## [0.0.1]
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)