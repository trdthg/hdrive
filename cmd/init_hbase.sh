#!/bin/sh
create 'stu_table', 'cf1', 'cf2', 'cf3'

put 'stu_table', 'aa', 'cf1:sname', 'aa'
put 'stu_table', 'aa', 'cf1:sgender', 'man'
put 'stu_table', 'aa', 'cf1:spass', '12345678'

put 'stu_table', 'bb', 'cf1:sname', 'bb'
put 'stu_table', 'bb', 'cf1:sgender', 'man'
put 'stu_table', 'bb', 'cf1:spass', '12345678'
