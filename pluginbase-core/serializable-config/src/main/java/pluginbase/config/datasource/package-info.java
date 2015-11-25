/**
 * This packages contains classes related to saving and loading data into various storage mediums.
 * <p/>
 * For human readable data files, Hocon is recommended as it is the most straight forward for the end user. Hocon also
 * supports comments through the {@link pluginbase.config.annotation.Comment} annotation. To create a Hocon data source
 * see {@link pluginbase.config.datasource.hocon.HoconDataSource#builder()}.
 * <p/>
 * <strong>Note:</strong> You must include ninja.leaping.configurate:configurate-core:3.0 or compatible libraries on the
 * classpath to use this package. Each data source implementation has additional library requirements. Refer to each
 * package for specific details.
 */
package pluginbase.config.datasource;