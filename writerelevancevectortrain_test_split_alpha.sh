LANG=en_US.utf8
cd java/
mkdir -p /data/sidana/recnet_draft/$1/recnet_alpha/$2/rv/
javac -cp binaries/commons-lang3-3.5.jar  preProcess/ConvertIntoRelVecGeneralized_update.java preProcess/InputOutput.java
if [ $2 == "one" ]
then
   rank=2
elif [ $2 == "five" ]
then
   rank=5
else
   rank=10
fi
java -cp . preProcess.ConvertIntoRelVecGeneralized_update /data/sidana/recnet_draft/$1/recnet_alpha/vectors/gt_$1 /data/sidana/recnet_draft/$1/recnet_alpha/vectors/pr_$1 /data/sidana/recnet_draft/$1/recnet_alpha/$2/rv/relevanceVector_$1 $rank
cd - >> uselessfile
python3 compOfflineEvalMetrics_len${rank}_batches.py /data/sidana/recnet_draft/$1/recnet_alpha/$2 "$1"
