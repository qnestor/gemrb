/* GemRB - Infinity Engine Emulator
 * Copyright (C) 2003 The GemRB Project
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * $Header: /data/gemrb/cvs2svn/gemrb/gemrb/gemrb/plugins/ACMImporter/unpacker.h,v 1.3 2004/01/02 15:56:47 balrog994 Exp $
 *
 */

#ifndef _ACM_LAB_VALUE_UNPACKER_H
#define _ACM_LAB_VALUE_UNPACKER_H

#include "../Core/DataStream.h"

class CValueUnpacker {
private:
// Parameters of ACM stream
	int levels, subblocks;
	//FILE* file;
	DataStream * stream;
// Bits
	unsigned long next_bits; // new bits
	int avail_bits; // count of new bits

	int sb_size, block_size;
	short *amp_buffer, *buff_middle;
	long* block_ptr;

// Reading routines
	void prepare_bits (int bits); // request bits
	long get_bits (int bits); // request and return next bits
public:
// These functions are used to fill the buffer with the amplitude values
	int return0 (int pass, int ind);
	int zero_fill (int pass, int ind);
	int linear_fill (int pass, int ind);

	int k1_3bits (int pass, int ind);
	int k1_2bits (int pass, int ind);
	int t1_5bits (int pass, int ind);

	int k2_4bits (int pass, int ind);
	int k2_3bits (int pass, int ind);
	int t2_7bits (int pass, int ind);

	int k3_5bits (int pass, int ind);
	int k3_4bits (int pass, int ind);

	int k4_5bits (int pass, int ind);
	int k4_4bits (int pass, int ind);

	int t3_7bits (int pass, int ind);


	CValueUnpacker (int lev_cnt, int sb_count, DataStream * stream)
		: levels (lev_cnt), subblocks (sb_count),
		next_bits (0), avail_bits (0),
		sb_size (1<< levels), block_size (sb_size * subblocks),
		amp_buffer (NULL), buff_middle (NULL), block_ptr (NULL) {
			this->stream = stream;
		};
	virtual ~CValueUnpacker() { if (amp_buffer) delete amp_buffer; };

	int init_unpacker();
	int get_one_block (long* block);
};

typedef int (CValueUnpacker::* FillerProc) (int pass, int ind);

#endif
