/**
 * int_length:  purely numerical computation of an integer's length
 * dgti:        returns the digit in an integer at position pos
 *  NOTE:       as a snippet only, for proper execution it requires a proper implementation of integer overflow controls
 */
typedef unsigned char       uchar;
typedef unsigned long long  ullong;
/// @TODO SEE BELOW THE FIXME TAG IN narc_num_count_in_rng
size_t int_length(ullong num)
{
// 	std::cerr << " -" <<num << std::endl;
	size_t len = 0;	// doesn't matter as long as it's less than the actual length of num
	while(num % (ulong) pow(10, len++) != num);
	// ALTERNATIVE: start with a large number for the size, e.g., which is known for a ulong ULONG_MAX
	return num == 0 ? 1 : len - 1;
}

/// @note: char for size only, debatable, will wind up as an int anyway. return type was also char but is long due to the assignment requirements
uchar dgti(ullong num, uchar pos)
{
// 	std::stringstream ss; ss << num;
// 	size_t len = std::string(ss.str()).length();		// TODO use int_length now instead
	size_t len = int_length(num);
	ullong right_cut = num / (llong) pow(10, len - pos);	// NOTE len-pos >= 0

	return (char) right_cut % 10;
}
