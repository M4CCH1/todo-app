name := """todo-app"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.18"

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += "com.mysql" % "mysql-connector-j" % "9.6.0"