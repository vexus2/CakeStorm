# CakeStorm
[日本語版ReadMEはこちら](https://github.com/nanapi/cake-storm/blob/master/README_ja.md)

CakeStorm is a [PhpStorm](http://www.jetbrains.com/phpstorm/) plugin for [CakePHP](http://wwww.cakephp.org) developers.

It provides the ability to easily jump between files.

![movie](https://github.com/nanapi/cake-storm/raw/master/images/presentation1.gif)

![movie](https://github.com/nanapi/cake-storm/raw/master/images/presentation2.gif)

![movie](https://github.com/nanapi/cake-storm/raw/master/images/presentation3.gif)

## Requirements
- PhpStorm 6.0.0+ or IntelliJ Idea 12.0+ with PHP Plugin
- CakePHP 1.3+ or 2.x+

## Installation
1. Go to File > Settings
2. Select Plugins option under "IDE Setting"
3. Click "Browse Repositories..." button
4. Search for CakeStorm
5. Right Click and select Download and Install
6. Save the settings and restart the IDE

## What's New
### Version 0.5.3
- Fixed bug.

### Version 0.5.2
- Fixed bug for PhpStorm7

### Version 0.5.1
- Fixed "Plugin" jump bug for CakePHP 1.3.X

### Version 0.5
- Support Smart Jump and Go to Declaration in "Plugin" Directory. (ex: /app/Plugin/Example/Controller/ExampleController)

### Version 0.4.3
- Fixed bug.

### Version 0.4.2
- Fixed bug.

### Version 0.4.1
- Add a references to Layouts files.
- Fixed bug.

### Version 0.4
- Add Open File in New Tab to "SmartJump".
- Default keymap is "C-; V" for Horizontal Open, "C-; H" for Vertical Open.

### Version 0.3.3
- Fixed PersistentStateComponent.

### Version 0.3.2
- Fixed bug.

### Version 0.3.1
- Fixed bug when first boot.

### Version 0.3
- Add a references to Model, View, to the Element from any files.

### Version 0.2
- The name of SmartJump has been changed to CakePHP SmartJump.
- Add View => Element jump for CakePHP SmartJump.
- CakePHP SmartJump will display a pop-up in any scene.
- CakePHP SmartJump can jump to Fixture <=> Model <=> ModelTestCase.
- Modified to display at the top of the list the current action.
- Fixed a bug in when arguments are included in the 'render'.

## Jump Command List
| Current File | Command | Jump to |
| --- | --- | --- |
| Controller|C-; s| View |
| View |C-; s| Controller |
| Component |C-; s| ComponentTestCase |
| Behavior |C-; s| BehaviorTestCase |
| Helper |C-; s| HelperTestCase |
| Fixture |C-; s | Model |

## FAQ
- I can not remember all those commands, there are too many.

You should at least memorize "CakePHP SmartJump"(Ctrl+; s).
This provides a jump to the right place if a certain file exists.

- How to jump to the View file?

Use PhpStorm's 'Go to declaration' action.
Default Keybind is "Ctrl+b"

- I found a bug. What should I do?

Please open an [issue](https://github.com/nanapi/cake-storm/issues) or fix it and send a pull request. :grin:
