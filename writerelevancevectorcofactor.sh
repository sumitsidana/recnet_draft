LANG=en_US.utf8
cd java/
mkdir -p /data/sidana/recnet_draft/$1/cofactor/five/rv/
mkdir -p /data/sidana/recnet_draft/$1/cofactor/five/em/
javac -cp binaries/commons-lang3-3.5.jar  preProcess/ConvertIntoRelVecGeneralized_update.java preProcess/InputOutput.java
echo 'making relevance vector'
java -cp . preProcess.ConvertIntoRelVecGeneralized_update /data/sidana/recnet_draft/$1/cofactor/vectors/gt_$1 /data/sidana/recnet_draft/$1/cofactor/vectors/pr_$1 /data/sidana/recnet_draft/$1/cofactor/five/rv/relevanceVector_$1 5
cd -
echo 'compute offline metrics'
python3 compOfflineEvalMetrics_len4.py /data/sidana/recnet_draft/$1/cofactor/five $1
