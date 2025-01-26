package com.yahoo.athenz.common.notification.slack;

public class SlackTokenLoaderFactory {

    /**
     * Creates an instance of TokenLoader based on the system property.
     *
     * @return An instance of TokenLoader.
     */
    public static TokenLoader createTokenLoader() throws Exception {
        String loaderClassName = System.getProperty("slack.token.loader.class");
        if (loaderClassName == null || loaderClassName.isEmpty()) {
            throw new Exception("System property 'slack.token.loader.class' is not set.");
        }

        try {
            Class<?> loaderClass = Class.forName(loaderClassName);
            if (!TokenLoader.class.isAssignableFrom(loaderClass)) {
                throw new Exception(loaderClassName + " does not implement TokenLoader interface.");
            }

            // For simplicity, assume a constructor that takes a single String parameter.
            // Modify as needed for different constructors.
            String param = System.getProperty("slack.token.loader.param1");
            if (param == null) {
                throw new Exception("Required constructor parameter 'slack.token.loader.param1' is missing.");
            }

            return (TokenLoader) loaderClass.getConstructor(String.class).newInstance(param);

        } catch (ClassNotFoundException e) {
            throw new Exception("TokenLoader class not found: " + loaderClassName, e);
        } catch (ReflectiveOperationException e) {
            throw new Exception("Failed to instantiate TokenLoader: " + loaderClassName, e);
        }
    }
}
