name := "test"
enablePlugins(DCOSPlugin)

val DCOSVersion = "0.4.13"

version in dcosCli := DCOSVersion

TaskKey[Unit]("check") := {
  val cliVersion = Seq(dcosCli.value.getAbsolutePath, "--version").lines_!
  assert(cliVersion.headOption == Some(s"dcoscli.version=$DCOSVersion"))
}
