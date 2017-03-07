# sbt-dcos

[![Travis CI Status]][Travis CI]
[![Bintray Latest Version Badge]][Bintray Latest Version]

Provides a managed [DC/OS CLI] binary.

## Usage

To use this library configure your sbt project with the following lines:
```sbt
addSbtPlugin("io.github.jonas" % "sbt-dcos" % "0.1.0")
```

Then use to deploy, for example to Marathon:
```sbt
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
```

## Releasing

To release version `x.y.z` run:

    > sbt release -Dproject.version=x.y.z

## License

sbt-dcos is licensed under the [Apache License, Version 2.0][apache] (the
"License"); you may not use this software except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 [apache]: http://www.apache.org/licenses/LICENSE-2.0
 [DC/OS CLI]: https://github.com/dcos/dcos-cli
 [Travis CI]: https://travis-ci.org/jonas/sbt-dcos
 [Travis CI Status]: https://travis-ci.org/jonas/sbt-dcos.svg?branch=master
 [Bintray Latest Version Badge]: https://api.bintray.com/packages/fonseca/maven/sbt-dcos/images/download.svg
 [Bintray Latest Version]: https://bintray.com/fonseca/maven/sbt-dcos/_latestVersion
