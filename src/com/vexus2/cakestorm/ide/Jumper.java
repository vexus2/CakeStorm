package com.vexus2.cakestorm.ide;

import com.intellij.openapi.actionSystem.AnActionEvent;

public class Jumper {

    public static void toController(AnActionEvent event) {

        System.out.println(event.getClass());

    }
}
