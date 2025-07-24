#!/bin/bash

# List of JMX ports used by HCM microservices
JMX_PORTS=(9011 9012 9013 9014 9015 9016 9017 9018)
# List of server ports used by HCM microservices
SERVER_PORTS=(9100 9108 9104 9112 9116 9124 9128 9120)

ALL_PORTS=("${JMX_PORTS[@]}" "${SERVER_PORTS[@]}")

echo "Killing Java processes using JMX or server ports: ${ALL_PORTS[*]}"

for port in "${ALL_PORTS[@]}"; do
  pid=$(lsof -ti tcp:$port)
  if [ -n "$pid" ]; then
    echo "Killing process $pid on port $port"
    kill -9 $pid
  else
    echo "No process found on port $port"
  fi
done

echo "Done." 