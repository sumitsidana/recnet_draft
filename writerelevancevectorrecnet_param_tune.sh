LANG=en_US.utf8
cd java/
mkdir -p /data/sidana/recnet/param_tune/$1/recnet/$3/rv/
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
java -cp . preProcess.ConvertIntoRelVecGeneralized_update /data/sidana/recnet/param_tune/$1/recnet/vectors/gt_$1_$2 /data/sidana/recnet/param_tune/$1/recnet/vectors/pr_$1_$2 /data/sidana/recnet/param_tune/$1/recnet/$3/rv/relevanceVector_$1_$2 $rank
cd - >> uselessfile
python3 compOfflineEvalMetrics_len${rank}_param_tune.py /data/sidana/recnet/param_tune/$1/recnet/$3 "$1_$2"
