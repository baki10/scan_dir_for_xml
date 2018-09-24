#!/bin/bash
for i in `seq 1 20`; 
do
     echo "<test>${i}</test>" > source/test${i}.xml
done
