LANG=en_US.utf8
cd java/
mkdir -p /data/sidana/recnet/recnet_batches/$1/$3/rv/
javac -cp binaries/commons-lang3-3.5.jar  preProcess/ConvertIntoRelVecGeneralized_update.java preProcess/InputOutput.java
if [ $3 == "one" ]
then
   rank=2
elif [ $3 == "five" ]
then
   rank=5
else
   rank=10
fi
java -cp . preProcess.ConvertIntoRelVecGeneralized_update /data/sidana/recnet/recnet_batches/$1/vectors/gt_$1_$2 /data/sidana/recnet/recnet_batches/$1/vectors/pr_$1_$2 /data/sidana/recnet/recnet_batches/$1/$3/rv/relevanceVector_$1_$2 $rank
cd - >> uselessfile
python3 compOfflineEvalMetrics_len${rank}_batches.py /data/sidana/recnet/recnet_batches/$1/$3 "$1_$2"
