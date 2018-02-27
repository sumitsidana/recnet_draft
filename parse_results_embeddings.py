# with open('results') as infile, open('results_output', 'w') as outfile:
#     for line in infile:
#         if not line.strip(): continue  # skip the empty line
#         outfile.write(line)  # non-empty line. Write it to output
	
# f = open('results_output', 'r')
import sys
f = open('/data/sidana/recnet/param_tune/'+sys.argv[1]+'/recnet/results', 'r')
f1 = open('/data/sidana/recnet/param_tune/'+sys.argv[1]+'/recnet/embeddings_'+sys.argv[1], 'w')

flag_latent_factor = True

flag_0_1 = False
flag_1_0 = False
flag_1_1 = False

flag_0_1_param_write=False
flag_1_0_param_write = False
flag_1_1_param_write = False

for line in f:

    line = line.strip('\n')

    if "Regularization: 0.005 Hidden Units: 64" in line:
        flag_0_1_param_write = True

    if "Regularization: 0.05 Hidden Units: 32" in line:
        flag_1_0_param_write = True

    if "Regularization: 0.005 Hidden Units: 16" in line:
        flag_1_1_param_write = True


    if line == "alpha: 0, beta: 1":
        flag_0_1 = True

    if line == "alpha: 1, beta: 0":
        flag_1_0 = True

    if line == "alpha: 1, beta: 1":
        flag_1_1 = True

    if "Latent Factor" in line:
        index = line.index(" ",15)
        latent_factor = line[15:index]
        if flag_latent_factor:
            f1.write(latent_factor + " ")
            flag_latent_factor = False
            # need to turn it into True after writing results of 1 1




    if "map@1:" in line:
        map1 = line[7:]
        if flag_1_1_param_write and flag_1_1:
            f1.write(map1+"\n")
            flag_1_1_param_write = False
            flag_1_1 = False
            flag_latent_factor = True
        else:
            if flag_0_1_param_write and flag_0_1:
                f1.write(map1+" ")
                flag_0_1_param_write = False
                flag_0_1 = False
            else:
                if flag_1_0_param_write and flag_1_0:
                    f1.write(map1+" ")
                    flag_1_0_param_write = False
                    flag_1_0 = False




f1.close()
f.close()