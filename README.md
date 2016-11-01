# Zuul Retryer

This is a sample application that will demonstrate how to customize retry logic when proxying requests through
Zuul.  In this sample we customize the default retry logic so that we will retry a request if the service returns
a `50x`.  You can use this sample as a base to add your own logic for your own services.

## Usage
You will need a Eureka server running as well in order to run this sample.

The Zuul server in this repo will proxy requests to itself via the `/self` endpoint.
There are two endpoints you can hit that can simulate failures/problems in a downstream service.

1.  `/timeout` will simulate a timeout occuring downstream.
2.  `/servererror` will simulate an internal server error from a downstream service.