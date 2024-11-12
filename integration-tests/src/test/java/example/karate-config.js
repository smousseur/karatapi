function fn() {
  var config = {
    appPort: process.env.appPort || '8080'
    appHost: process.env.appPort || 'localhost'
  };
  return config;
}