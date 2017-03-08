name := "sbt-dcos"
organization := "io.github.jonas"
description := "DC/OS command line wrapper"
homepage := Some(url("https://github.com/jonas/sbt-dcos"))
licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")

sbtPlugin := true
scriptedSettings
scriptedLaunchOpts += "-Dproject.version=" + version.value
// scriptedBufferLog := false

enablePlugins(GitPlugin)
versionWithGit
git.useGitDescribe := true

enablePlugins(BintrayPlugin)
bintrayRepository := "sbt-plugins"
bintrayOrganization := Some("fonseca")
publishArtifact in Test := false
publishMavenStyle := false

enablePlugins(ReleasePlugin)
releaseCrossBuild := true
releaseTagName := (version in ThisBuild).value
releaseVersionFile := target.value / "unused-version.sbt"
releaseProcess := {
  import ReleaseTransformations._
  Seq[ReleaseStep](
    checkSnapshotDependencies,
    { st: State =>
      val v = (version in ThisBuild).value
      st.put(ReleaseKeys.versions, (v, v))
    },
    runTest,
    setReleaseVersion,
    tagRelease,
    publishArtifacts,
    pushChanges
  )
}

autoAPIMappings := true
scalacOptions in (Compile,doc) := Seq(
  "-groups",
  "-implicits",
  "-doc-source-url", scmInfo.value.get.browseUrl + "/tree/master€{FILE_PATH}.scala",
  "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:postfixOps",
  "-unchecked",
  "-Xfuture",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen"
)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/jonas/sbt-dcos"),
    "scm:git:git@github.com:jonas/sbt-dcos.git"
  )
)

developers := List(
  Developer("jonas", "Jonas Fonseca", "jonas.fonseca@gmail.com", url("https://github.com/jonas"))
)
