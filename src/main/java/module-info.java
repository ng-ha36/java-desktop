module sample.asm2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

   opens sample.asm2.controller;
   opens sample.asm2.dao;
   opens sample.asm2.entity;
   opens ui;
   opens sample.asm2 to javafx.graphics;
}