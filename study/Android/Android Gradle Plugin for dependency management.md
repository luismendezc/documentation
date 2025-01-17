THERE IS A PROJECT IN THIS REPOSITORY CALLED (android_apps/GradlePluginDependencyAnalysis)

use the task in the gradle or this command ./gradlew buildHealth

LINK TO DOCU:
https://medium.com/bilue/caring-for-your-dependency-garden-an-approach-to-android-dependency-management-366b484d37b3

So let’s make use of the very useful [Dependency Analysis Plugin](https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin) (full documentation [here](https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/wiki)). This plugin will provide a report on the dependency usage for the app or SDK, it will provide the advice as follows (from their documentation):

> Dependency-related advice
> 
> Unused dependencies which should be removed.
> 
> Declared dependencies which are on the wrong configuration (api vs implementation vs compileOnly). This is variant-aware, so it might tell you to use debugImplementation, for example.
> 
> Transitively used dependencies which ought to be declared directly, and on which configuration.
> 
> Dependencies which could be declared on the compileOnly configuration, as they’re not required at runtime.
> 
> Annotation processors which could be removed as unused.
> 
> Plugin-related advice
> 
> Plugins that are applied but which can be removed. (currently we support kapt, java-library, and kotlin-jvm for redundancy-checking)

To add the plugin, it is as simple as just adding a plugin block to your root `build.gradle` (with `dependency_analysis_plugin_version` being the current plugin version, found [here](https://plugins.gradle.org/plugin/com.autonomousapps.dependency-analysis) — `1.10.0` at time of writing):

You can also configure how the plugin will behave for each issue type — this helps when running this from your CI/CD set up (more on this later). By default the plugin will just give a warning, but as we know, warnings often get ignored!

My recommended project root level configuration for the plugin are as follows. The below configuration will run for all modules (see [documentation](https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/wiki/Customizing-plugin-behavior#failure-conditions-and-filtering) for how to exclude or configure specific modules).

The report can be run as a gradle task as using `./gradlew buildHealth` and a report with suggestions will be printed in the console.

Note: the minimum required version of Gradle is 7.0. For other limitations see [here](https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/wiki/Compatibilities-&-Limitations).

## Exclusions / Special Cases

Where needed we can add in exclusion rules or special cases if a particular library’s usage is not being picked up by the tool. This can be excluded on a library and/or module basis and can be done when there is no alternative.

It is best to use the exclusion rules rather than disabling the tool entirely and be as specific as possible.

An example of this:

For information on this kind of filtering & exclusion rules see [here](https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/wiki/Customizing-plugin-behavior).

## Example Output

## Alternatives

The [Nebula (Gradle Lint) Plugin](https://github.com/nebula-plugins/gradle-lint-plugin) exists but due to some open [issues](https://github.com/nebula-plugins/gradle-lint-plugin/issues/342) this is not able to be used on Android projects. This could be revisited if the plugin gets updated to support Android projects as this plugin is more well known & has more features (including gradle tasks that will automatically fix issues).

If you are aware of an alternative that provides better or more features that can be automated into the CI or development process please share in the comments!

# CI Integration

This can be set up on most CIs that allow you to run a script or specific gradle tasks.

My approach here is to fail the CI build if there are any recommendations from the plugin except for Transitive Dependencies which will be reported at a warning level (see configuration listed above). The thinking behind this is that the use of the parent dependency may still be in development (and the usage will be expanded in the future) or that the parent dependency is more well known and documented for use (i.e. searching for the release notes of the sub dependency may not be as easy as the parent dependency). It is recommended that these warnings be reviewed by the development team and adjusted as needed depending on the use case.

The CI script below will create both the machine readable json reports and a more human friendly report to `build/reportes/dependency-analysis/`. These can then be exposed as artefacts in the CI.

The CI task to complete this analysis should be done immediately after building (prior to running tests or other analysis) so that the build result is shared back to the team as soon as possible so can be fixed without causing the build to spend time running tests and delaying the failure report.

## CI job script

An example script that can be used to to pass the failure to a CI such as [Bitrise](https://www.bitrise.io/) is as follows:

`$PIPESTATUS` variable is used as `tee` does not output the task result.

-------

MORE IMPORTANT DOCU:
https://developer.android.com/build/dependency-upgrade-tools
https://github.com/autonomousapps/dependency-analysis-gradle-plugin?tab=readme-ov-file
https://github.com/autonomousapps/dependency-analysis-gradle-plugin/wiki/Customizing-plugin-behavior
https://github.com/autonomousapps/dependency-analysis-gradle-plugin/wiki/Adding-to-your-project


