# CakeStorm
CakeStormは[PhpStorm](http://www.jetbrains.com/phpstorm/)の[CakePHP](http://wwww.cakephp.org)ユーザ用のプラグインです。

主にファイル間のジャンプなどを便利にしてくれます。

![movie](https://github.com/nanapi/cake-storm/raw/master/images/presentation1.gif)

![movie](https://github.com/nanapi/cake-storm/raw/master/images/presentation2.gif)

![movie](https://github.com/nanapi/cake-storm/raw/master/images/presentation3.gif)

## 動作環境
- PhpStorm 6.0.0+ or IntelliJ Idea 12.0+(PHP Plugin必須)
- CakePHP 1.3+ or 2.x+

## インストール
1. 設定メニューから[IDE Settings]-[Plugins]を表示
2. [Browse repositories...]を選択
3. 右上の選択窓に「Cake」を入力し、[CakeStorm]を選択
4. 右クリックから[Download and Install]を選択し、[Close]を押下
5. IDEの再起動を聞かれるので[Restart]を押下

## 更新履歴
### Version 0.5.2
 - PhpStorm7向けにいくつかの不具合を修正しました。

### Version 0.5.1
- CakePHP1.3計で"Plugin"ディレクトリ内でのSmart Jumpが動作しなかった不具合を修正しました。

### Version 0.5
- "Plugin"ディレクトリ配下でのSmart Jumpをサポートしました。
    - /app/Plugin/Example 配下のディレクトリ内でSmart JumpやGo to Decralationコマンドが利用可能になります。

### Version 0.4.3
- 2つ以上の単語から構成されるController(ex: HogeFugaController)からrenderしているctpにジャンプ出来ない不具合を修正しました。

### Version 0.4.2
- シングルクォート文字列内でCtrl+Space押下時にNullpointerExxceptionが発生していた不具合を修正しました。

### Version 0.4.1
- $this->layout等で指定されているlayoutsファイルへCtrl+Bからのジャンプを追加しました。
- 特定ファイル内でSmartJumpを実行した際にNullpointerExceptionが発生していた不具合を修正しました。

### Version 0.4
- SmartJumpに「ファイルを新しいタブで開く」機能を追加しました。
- 水平分割で開く場合は"C-; H"、垂直分割で開く場合は"C-; V"で呼び出せます。

### Version 0.3.3
- 細かなパフォーマンス改善を行いました。

### Version 0.3.2
- 細かな不具合を修正しました。

### Version 0.3.1
- 初回インストール時にNullPointerExceptionが発生する不具合を修正しました。

### Version 0.3
- ファイル内の文字列からModelやView、Elementへのファイル参照を追加しました。これによりGo to Decralation(Ctrl+B or Ctrl+クリック)でのジャンプが可能になります。

### Version 0.2
- SmartJumpをCakePHP SmartJumpに名称変更。これに伴い[Find Action]にて「Cake」と入力すればSmart Jumpがサジェストされます。
- View→Elementへのジャンプを追加(Smart Jumpにて実行可能)。
- SmartJumpを共通にし、どのファイルからでもポップアップが表示されるように変更。
- Jump to Viewを実行した際、自身のいるアクションをリストの最上部に表示されるように変更。
- SmartJumpにFixture⇔Model⇔ModelTestCaseへ相互にジャンプ機能を追加。
- renderに第二引数が含まれている場合に正しくジャンプ出来ない箇所を修正。

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
- コマンドが多すぎて覚えられないんですけど？

とりあえず"CakePHP SmartJump"だけ覚えておけば事足ります。
デフォルトのキーマップは"Ctrl+; s"です。
SmartJumpは現在いるファイルから飛ぶことが出来るファイル一覧を表示します。

- 参照しているファイルへの移動はどうすれば？

PhpStormのデフォルト機能、"Go to Decralation"で移動出来ます。
デフォルトのキーマップは"Ctrl+B"です。

- バグってて動かないんだけど？

[Twitter](https://twitter.com/vexus2)か、[issue](https://github.com/nanapi/cake-storm/issues)で報告して頂けると助かります！
修正してPull Requestも大歓迎です。:grin:
