/**
 * This packages provides a YAML implementation of {@link pluginbase.config.datasource.DataSource}.
 * <br>
 * <strong>Warning:</strong> This implementation tries to support comments through {@link pluginbase.config.annotation.Comment}
 * though it may not always work and throw exceptions depending on the complexity of the yaml. For commented configuration
 * files, the Hocon package is much more reliable and performant.
 * <br>
 * <strong>Note:</strong> You must include org.yaml:snakeyaml:1.16 and ninja.leaping.configurate:configurate-yaml:3.0
 * or compatible libraries on the classpath to use this package.
 */
package pluginbase.config.datasource.yaml;