# CakeStorm
CakeStorm is a [PhpStorm](http://www.jetbrains.com/phpstorm/) plugin for [CakePHP](http://wwww.cakephp.org) developers.

It provides the ability to easily jump between files.

![movie](https://github.com/nanapi/cake-storm/raw/master/images/presentation1.gif)

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

## Jump Command List
| Current File | Command | Jump to |
| --- | --- | --- |
| Controller|C-; s| View |
| Controller|C-; C-c| ControllerTestCase |
| Model|C-; C-m| ModelTestCase |
| Model|C-; C-f| Fixture |
| View |C-; s| Controller |
| View |C-; c| Controller |
| Component |C-; s| ComponentTestCase |
| Component |C-; C-p| ComponentTestCase |
| Behavior |C-; s| BehaviorTestCase |
| Behavior |C-; C-b| BehaviorTestCase |
| Helper |C-; s| HelperTestCase |
| Helper |C-; C-h| HelperTestCase |
| Fixture |C-; s | Model |
| Fixture |C-; m | Model |
| ControllerTestCase |C-; h| Controller |
| ModelTestCase |C-; m| Model |
| ComponentTestCase |C-; p| Component |
| BehaviorTestCase |C-; b| Behavior |
| HelperTestCase |C-; h| Helper |
| Any File | C-; C-t | Any Test File |

## FAQ
- I can not remember all those commands, there are too many.

You should at least memorize "Smart Type"(Ctrl+; s).
This provides a jump to the right place if a certain file exists.

- I found a bug. What should I do?

Please open an [issue]() or fix it and send a pull request. :grin:
