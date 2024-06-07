.data
prompt_string: .asciiz "An element has been stored. Will it load properly!?!?\n"
storage: .space 8

.text 
main:
	# store some value in storage
    	addi $sp, $sp, -4 
   	add $t1, $0, 'a'
   	sw $t1, 0($sp)
   
   	# do something like print out a prompt
   	la $a0, prompt_string #set a0 to point to prompt_string
   	li $v0, 4 # syscall for printing string
   	syscall #print out prompt_string
   	
   	lw $a0, ($sp)
    	addi $sp, $sp, -4 
    	addi $v0, $0, 11 # Syscall to print integer
    	syscall # Print out value in $a0   ($t2)
   	
   	
   	# should be 97
   
   
