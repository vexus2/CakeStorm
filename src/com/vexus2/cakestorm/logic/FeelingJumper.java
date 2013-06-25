package com.vexus2.cakestorm.logic;

import com.intellij.openapi.actionSystem.AnActionEvent;

public class FeelingJumper extends Jumper {
  public FeelingJumper(AnActionEvent e) throws Exception {
    super(e);
  }

  @Override
  public void jump() {
    switch (fileSystem.getType()) {
      case Controller:
        //TODO: render()しているview、アクション名のview、TestCaseのいずれか
        break;
      case View:
        // TODO: Controller(存在すれば)
        break;
      case Model:
        //TODO: TestCase, Fixture
        break;
      case Helper:
        //TODO: HelperTestCase
        break;
      case Component:
        //TODO: ComponentTestCase
        break;
      case Behavior:
        //TODO: BehaviorTestCase
        break;
      case Shell:
        //TODO: POPUP NONE
        break;
      case Task:
        //TODO: POPUP NONE
        break;
      case Fixture:
        //TODO: Model
        break;
      case ControllerTestCase:
        //TODO: Controller
        break;
      case ModelTestCase:
        //TODO: Model
        break;
      case BehaviorTestCase:
        //TODO: Behavior
        break;
      case ComponentTestCase:
        //TODO: Component
        break;
      case HelperTestCase:
        //TODO: Helper
        break;
    }
  }
}
