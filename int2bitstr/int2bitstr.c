/*A function that stores the bit pattern of a 32 bit integer as a string of binary 1 or 0 of the same length */
void int2bitstr(int I, char *str) {
        /*the intialization of an int that has the stored value of 31 as that is the last used index of the
        array used to create the string for the 32-bit binary. */
        int length = 31;
        /* sets the 32nd index of the array as the end of the string through null termination */
        str[32] = '\0';;
        /* while loop that is used to add all 32-bits to the array in the proper order from
        least significant bit to the most significant bit */
        while (length >= 0)
        {
                /* int that is set to the value of result of the operation I & 1 which compares
                the least significant bit of the integer provided to 1 to see whether or not
                a 1 or 0 should occupy the current bit value being evaluated. */
                int checker = I & 1;
                /* sets the value of the current index to the character 1 when checker results in 1*/
                if(checker == 1) str[length] = '1';
                /* sets the value of the index to the character 0 when the operand evaluted in checker
                does not result in 1 */
                else str[length] = '0';
                /*decrements the value of int length in order to proceed to the next index of the array
                that must be filled and evaluated for */
                length--;
                /* shifts the 32-bit integer to the right once removing the last least significant bit
                used for comparison with 1 in the operand for int checker */
                I=I>>1;
        }

}

/*A function  that calculates the exponent value of float f and returns it.
the input for f is given by the user */
int get_exp_value(float f) {
        /*retrieves the bit pattern for float f.*/
        unsigned f2u(float f);
        unsigned int ui = f2u(f);
        /* left shifts the unsigned integer by one space in order to remove the sign bit from
        the floating point bit pattern*/
        ui = ui<<1;
        /* right shifts the unsigned integer by 24 spaces in order to leave only the 8 most
        signifcant bits of the floating point bit pattern */
        ui = ui>>24;
        /* special case for when all bits are 1s */
        if(ui==225)
        return -128;
        /*special case for when all the bits of the pattern are 0s */
        else if(ui == 0)
        return 1-127;
        /* finds the differnce of the bias and the 8 bit value found */
        else
        return ui-127;


}

