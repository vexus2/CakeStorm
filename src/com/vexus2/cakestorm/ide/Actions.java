package com.vexus2.cakestorm.ide;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

@SuppressWarnings("ComponentNotRegistered")
public class Actions {
    public static class JumpToController extends AnAction {
        @Override
        public void actionPerformed(AnActionEvent event) {
            Jumper.toController(event);
        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
        }
    }

    public static class JumpToModel extends AnAction {
        @Override public void actionPerformed(AnActionEvent event) {

        }

        @Override public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
        }
    }

    public static class JumpToView extends AnAction {
        @Override
        public void actionPerformed(AnActionEvent event) {

        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
        }
    }

    public static class JumpToComponent extends AnAction {
        @Override
        public void actionPerformed(AnActionEvent event) {

        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
        }
    }

    public static class JumpToBehavior extends AnAction {
        @Override
        public void actionPerformed(AnActionEvent event) {

        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
        }
    }

    public static class JumpToHelper extends AnAction {
        @Override
        public void actionPerformed(AnActionEvent event) {

        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
        }
    }

    public static class JumpToFixture extends AnAction {
        @Override
        public void actionPerformed(AnActionEvent event) {

        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
        }
    }

    public static class JumpToTestCase extends AnAction {
        @Override
        public void actionPerformed(AnActionEvent event) {

        }

        @Override
        public void update(AnActionEvent e) {
            e.getPresentation().setEnabled(PlatformDataKeys.EDITOR.getData(e.getDataContext()) != null);
        }
    }

}
