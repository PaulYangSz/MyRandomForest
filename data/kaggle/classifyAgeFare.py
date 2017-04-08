# -*- coding: utf-8 -*-
"""
Created on Sat Apr 08 15:32:58 2017

@author: Administrator
"""

# -*- coding: utf-8 -*-
# 这个是从HanXiaoyang的Titanic.ipynb中提取出的数据预处理方法,不然用Java写太费劲了。

import pandas as pd #数据分析
import numpy as np #科学计算
from pandas import Series,DataFrame

data_train = pd.read_csv("gen_train_str_data.csv")

#Show first line contents.
data_train.columns

#Show every fields's count.
data_train.info()

#Show mean mix max and so on...
data_train.describe()



def set_AgeRang_type(df):
    for i in range(0, len(df)):
        if df.Age[i] < 1:
            strClass = 0
        elif df.Age[i] < 5:
            strClass = 5
        elif df.Age[i] < 10:
            strClass = 10
        elif df.Age[i] < 20:
            strClass = 20
        elif df.Age[i] < 30:
            strClass = 30
        elif df.Age[i] < 40:
            strClass = 40
        elif df.Age[i] < 50:
            strClass = 50
        elif df.Age[i] < 60:
            strClass = 60
        else:
            strClass = 70
            
        df.set_value(i,'Age',strClass)
        
    return df


def set_FareRang_type(df):
    for i in range(0, len(df)):
        if df.Fare[i] == 0:
            strClass = 0
        elif df.Fare[i] < 10:
            strClass = 10
        elif df.Fare[i] < 20:
            strClass = 20
        elif df.Fare[i] < 30:
            strClass = 30
        elif df.Fare[i] < 40:
            strClass = 40
        elif df.Fare[i] < 50:
            strClass = 50
        elif df.Fare[i] < 60:
            strClass = 60
        elif df.Fare[i] < 70:
            strClass = 70
        elif df.Fare[i] < 100:
            strClass = 100
        elif df.Fare[i] < 300:
            strClass = 200
        else:
            strClass = 500
            
        df.set_value(i,'Fare',strClass)
        
    return df

data_train = set_AgeRang_type(data_train)
data_train = set_FareRang_type(data_train)


print "data_train.head()"
print data_train.head()

age_count = data_train[u'Age'].value_counts()
#print age_count
#ageFig = age_count.plot(kind='bar').get_figure()
#ageFig.show()
print data_train[u'Age'].describe()
#data_train.boxplot(column="Age")

fare_count = data_train[u'Fare'].value_counts()
FarePic = data_train.boxplot(column="Fare")
#print FarePic
#FarePic.savefig('fare.png');
data_train.to_csv('gen_train_AgeFare.csv', index=False)


#Begin to process test data
data_test = pd.read_csv("gen_test_str_data.csv")
print "Original gen_test_str_data.csv"
data_test.info()

data_test = set_AgeRang_type(data_test)
data_test = set_FareRang_type(data_test)

ageTest_count = data_test[u'Age'].value_counts()
fareTest_count = data_test[u'Fare'].value_counts()
#print "After making-up the Fare, Age and Cabin"
#test.info()
data_test.to_csv('gen_test_AgeFare.csv', index=False)
