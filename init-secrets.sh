#!/bin/bash

sleep 5

vault login j3H8p2T5mQ9bF6vY4wZ1R0cG7S

vault kv put secret/application \
  database="InmoproV1" \
  dbpassword="AdminInmoProIshere1!" \
  dbuser="InmoProAdmin@inmopro" \
  emailApp="inmopro.comuniciones@gmail.com" \
  passwordApp="zqbb xulv hfzu nvls"
