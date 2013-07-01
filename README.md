# CakeStorm
CakeStorm is a PhpStorm plugin for CakePHP developers.

Provides the ability to easily jump between files.

![movie](http://plugins.jetbrains.com/files/7277/screenshot_14246.png)

## Requirements
- PhpStorm 6.0.0+
- IntelliJ Idea 12.0+ with PHP Plugin
- CakePHP ver.1.3+ or ver.2.0+

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
- I can not remember commands which you should remember there are many.

You should memorize only "Smart Type"(Ctrl+; s).
This provides a jump to the perfect place if exists a file.

- I found bug. What should I do?

Please open an [issue]() or fix it and send pull request. :grin: