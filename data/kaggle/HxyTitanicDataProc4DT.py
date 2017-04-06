# -*- coding: utf-8 -*-
# 这个是从HanXiaoyang的Titanic.ipynb中提取出的数据预处理方法,不然用Java写太费劲了。

import pandas as pd #数据分析
import numpy as np #科学计算
from pandas import Series,DataFrame

data_train = pd.read_csv("Train.csv")

#Show first line contents.
data_train.columns

#Show every fields's count.
data_train.info()

#Show mean mix max and so on...
data_train.describe()


#Show a plot
import matplotlib.pyplot as plt
from matplotlib.font_manager import FontProperties
plt.rcParams['font.sans-serif']=['SimHei'] #用来正常显示中文标签
plt.rcParams['axes.unicode_minus']=False #用来正常显示负号
fig = plt.figure()
fig.set(alpha=0.2)  # 设定图表颜色alpha参数

plt.subplot2grid((2,3),(0,0))             # 在一张大图里分列几个小图
data_train.Survived.value_counts().plot(kind='bar')# plots a bar graph of those who surived vs those who did not. 
plt.title(u"获救情况 (1为获救)") # puts a title on our graph
plt.ylabel(u"人数")  

plt.subplot2grid((2,3),(0,1))
data_train.Pclass.value_counts().plot(kind="bar")
plt.ylabel(u"人数")
plt.title(u"乘客等级分布")

plt.subplot2grid((2,3),(0,2))
plt.scatter(data_train.Survived, data_train.Age)
plt.ylabel(u"年龄")                         # sets the y axis lable
plt.grid(b=True, which='major', axis='y') # formats the grid line style of our graphs
plt.title(u"按年龄看获救分布 (1为获救)")


plt.subplot2grid((2,3),(1,0), colspan=2)
data_train.Age[data_train.Pclass == 1].plot(kind='kde')   # plots a kernel desnsity estimate of the subset of the 1st class passanges's age
data_train.Age[data_train.Pclass == 2].plot(kind='kde')
data_train.Age[data_train.Pclass == 3].plot(kind='kde')
plt.xlabel(u"年龄")# plots an axis lable
plt.ylabel(u"密度") 
plt.title(u"各等级的乘客年龄分布")
plt.legend((u'头等舱', u'2等舱',u'3等舱'),loc='best') # sets our legend for our graph.


plt.subplot2grid((2,3),(1,2))
data_train.Embarked.value_counts().plot(kind='bar')
plt.title(u"各登船口岸上船人数")
plt.ylabel(u"人数")  
plt.show()





#我们这里用scikit-learn中的RandomForest来拟合一下缺失的年龄数据
from sklearn.ensemble import RandomForestRegressor
 
### 使用 RandomForestClassifier 填补缺失的年龄属性
def set_missing_ages(df):
    
    # 把已有的数值型特征取出来丢进Random Forest Regressor中
    age_df = df[['Age','Fare', 'Parch', 'SibSp', 'Pclass']]

    # 乘客分成已知年龄和未知年龄两部分
    known_age = age_df[age_df.Age.notnull()].as_matrix()
    unknown_age = age_df[age_df.Age.isnull()].as_matrix()

    # y即目标年龄
    y = known_age[:, 0]

    # X即特征属性值
    X = known_age[:, 1:]

    # fit到RandomForestRegressor之中
    rfr = RandomForestRegressor(random_state=0, n_estimators=2000, n_jobs=-1)
    rfr.fit(X, y)
    
    # 用得到的模型进行未知年龄结果预测
    predictedAges = rfr.predict(unknown_age[:, 1::])
    
    # 用得到的预测结果填补原缺失数据
    df.loc[ (df.Age.isnull()), 'Age' ] = predictedAges 
    
    return df, rfr

def set_Cabin_type(df):
    df.loc[ (df.Cabin.notnull()), 'Cabin' ] = "Yes"
    df.loc[ (df.Cabin.isnull()), 'Cabin' ] = "No"
    return df

def set_Embarked_type(df):
    df.loc[ (df.Embarked.isnull()), 'Embarked' ] = "Null"
    return df

def set_AgeRang_type(df):
    if(df.loc[df.Age, 'Age'] < 10):
        df.loc[df.Age, 'Age'] = 1
    if(df.loc[df.Age, 'Age'] < 20):
        df.loc[df.Age, 'Age'] = 2
    if(df.loc[df.Age, 'Age'] < 30):
        df.loc[df.Age, 'Age'] = 3
    if(df.loc[df.Age, 'Age'] < 40):
        df.loc[df.Age, 'Age'] = 4
    if(df.loc[df.Age, 'Age'] < 50):
        df.loc[df.Age, 'Age'] = 5
    if(df.loc[df.Age, 'Age'] < 60):
        df.loc[df.Age, 'Age'] = 6
    if(df.loc[df.Age, 'Age'] < 90):
        df.loc[df.Age, 'Age'] = 7

data_train, rfr = set_missing_ages(data_train)
data_train = set_Cabin_type(data_train)
data_train = set_Embarked_type(data_train)
#data_train = set_AgeRang_type(data_train)
data_train.info()



data_train = data_train.filter(regex='Survived|Age|SibSp|Parch|Fare|Cabin|Embarked|Sex|Pclass')
cols = data_train.columns.tolist()
cols = cols[1:] + cols[:1]
cols
data_train = data_train[cols]
data_train.info()
print "data_train.head()"
print data_train.head()
data_train.to_csv('gen_train_str_data.csv', index=False)

age_count = data_train[u'Age'].value_counts()
#print age_count
ageFig = age_count.plot(kind='bar').get_figure()
ageFig.savefig('age.png');
print data_train[u'Age'].describe()
#data_train.boxplot(column="Age")

FarePic = data_train.boxplot(column="Fare")
#FarePic.savefig('fare.png');




#Begin to process test data
data_test = pd.read_csv("test.csv")
print "Original test.csv"
data_test.info()
data_test.loc[ (data_test.Fare.isnull()), 'Fare' ] = 0

# 接着我们对test_data做和train_data中一致的特征变换
# 首先用同样的RandomForestRegressor模型填上丢失的年龄
tmp_df = data_test[['Age','Fare', 'Parch', 'SibSp', 'Pclass']]
null_age = tmp_df[data_test.Age.isnull()].as_matrix()
# 根据特征属性X预测年龄并补上
X = null_age[:, 1:]
predictedAges = rfr.predict(X)
data_test.loc[ (data_test.Age.isnull()), 'Age' ] = predictedAges

# 把Cabin数据按照yes, no分类赋值。
data_test = set_Cabin_type(data_test)


test = data_test.filter(regex='Age|SibSp|Parch|Fare|Cabin|Embarked|Sex|Pclass')
print "After making-up the Fare, Age and Cabin"
test.info()
test.to_csv('gen_test_str_data.csv', index=False)