# MyRandomForest
Learn random forest and predict the Kaggle-Titanic

## 代码框架简介
src是代码文件夹，data是数据文件夹
### data/kaggle
这个目录下面的train.csv和test.csv是从kaggle上下载的Titanic预测问题的原始数据。
然后用HxyTitanicDataProc4DT.py对缺失数据进行了补充。
然后用classifyAgeFare.py对Age和Fare数据进行了分类归纳处理。
### src
#### TitanicMain
这个是工程的主入口类文件。
- 首先读取处理过的训练数据文件创建出StrFileHelper的对象
- 其次用这个StrFileHelper的对象来构造出TrainStrDataSet的对象
- 再次构造一个ID3/C4.5算法的对象，并给出阈值Epsilon的值（这个值是通过前几次运行不断尝试得到的。）
- 再再次通过前面得到的TrainStrDataSet、ID3/C4.5以及一个给出的选取特征X的String创建出一个RandomForestModel的对象，并开始训练得到一个随机森林。
- 最后用训练后的模型预测原声训练数据，统计正确率。
- 最最后用这个模型预测test.csv（不是原始数据，是处理后的），得到满足Kaggle提交格式要求的submission数据。
#### StrFileHelper
这个类是从处理后的train.csv文件中读取，保存行数信息，X的名字信息，列数信息，以及每行的Stringp[]信息。
#### TrainStrDataSet
- 能从StrFileHelper中创建出一个有行数列数信息、Y和各个X_i的取值空间信息、X_i的名字信息、以及具体的每行数据的X_i和Y_i的数据。
- 能提供对TrainStrDataSet对象获取是否属于一个Y_i值的方法。
- 能获取这个TrainStrDataSet对象数据集中最多的Y_i的值。
- 能根据指定的X_i及其指定的取值获得相应的数据子集。
- 可以协助随机森林产生一个Bagging数据集。
- 提供一个打印功能，方便调试。
#### DevisionMethod
这个是一个抽象类，可以派生出具体的ID3或者C4.5算法的子类。并提供计算Gain或者Gratio的抽象方法，以及给决策树用的选择最佳分支特征的方法。
- ID3Algo
这个类继承了DevisionMethod，除了实现其抽象方法还提供了计算熵信息的方法。
- C4p5Algo
这个类继续继承ID3Algo，并重载了计算Gratio和选取最佳分支特征的方法。
#### RandomForestModel
这个类是实现了随机森林的算法。
- 从给定的TrainStrDataSet、备选特征向量、ID3/C4.5中创建一个对象。
- 根据数据集产生出T棵决策树组成的随机森林，其中对于每棵树都有一个随机产生的1个数据集，1个用于选取分支特征的特征子向量。
- 对于输入的X[]，根据训练得到的随机森林预测结果。
##### DecisionTreeModel
这个是在随机森林中用到的决策树。
- 包含一个描述数结构的内部类
- 产生数的方法
- 预测结果的方法

## 预测结果以及一些思考
### 预测结果
RF最终产生的结果和单棵树的结果是基本一样的。也就是随机的效果没有体现出来。
猜测可能是因为训练数据集的量太小了，一棵树就很容易收敛了，多棵随机树的结果和一棵树的结果也就一样了。
### 代码优化
在产生训练子集的过程中有很多循环，其中可以在循环中放入一些别的计算内容来提高计算效率。
而且在计算Entropy、Gain以及Gratio中都有几处重复计算的地方，可以重新考虑在一、二个循环里面计算完成。虽然这样会使代码变得难读，但是对于提高计算效率来说还是很明显的。
### 继续Titanic预测
整体来说DT和RF在这个问题上给出的预测结果都不如LR的预测结果。
而且RF和DT结果一致，AdaBoost的结果略低于LR的结果都表明Ensemble在Titanic问题上表现不佳。或许还是数据集太小的缘故吧？
如果要继续使用集成学习的思想，或许就不能继续在一个算法上ensemble而是需要多个不同的算法来做。
另外有一点也非常重要，就是数据特征，目前这个数据特征的工程还是太太太简单了。如果想要提升还需要花功夫分析，至少也要看看别人的方案。
