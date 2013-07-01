# CakeStorm
CakeStorm is a PHPStorm plugin for CakePHP developers.
Provides the ability to easily jump between files.

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

## Features
ファイル間のジャンプを提供してくれます。

困ったときにはスマートジャンプを使用してください。適切なファイルへのジャンプを行います。

CakeStormのすべての動作のプレフィックスは Ctrl + ;です。

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

## Notes

ModelからModel、ModelからControllerへのdecalaration(Ctrl + B)によるジャンプ
Jump Model From Controller like gf.
show model list with popup.
jump to elements from view.
autocomplete.

## Any problem?
Please open an [issue](https://github.com/nanapi/cake-storm/issues).