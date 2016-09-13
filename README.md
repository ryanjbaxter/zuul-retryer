# Zuul Retryer

This is a sample application that will demonstrate how to customize retry like when proxying requests through
Zuul.  In this sample we customize the default retry logic so that we will retry a request if the service returns
a `50x`.  You can use this sample as a base to add your own logic for your own services.

## Usage
Set the routes for the proxy in `application.yml`.  For each route you can specify if you want to it be retryable using
the `retryable` property.  See the comments in `application.yml` for more information.