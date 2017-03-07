name := "test"
enablePlugins(DCOSPlugin)

val deploy = taskKey[Unit]("Deploy app")

deploy := {
  val appId = name.value
  val json = baseDirectory.value / "marathon.json"

  def marathon(args: String*) =
    Process(dcosCli.value.getAbsolutePath :: "marathon" :: args.toList)

  if (marathon("task", "list", appId).!!.contains(appId))
    marathon("app", "update", "--force", appId) #< json ! streams.value.log
  else
    marathon("app", "add") #< json ! streams.value.log
  ()
}

TaskKey[Unit]("check") := {
  // FIXME
}
