	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p1_m2p0_a2p1_c2p0"
	.file	"builtin.c"
	.globl	_array.size                     # -- Begin function _array.size
	.p2align	1
	.type	_array.size,@function
_array.size:                            # @_array.size
# %bb.0:
	lw	a0, -4(a0)
	ret
.Lfunc_end0:
	.size	_array.size, .Lfunc_end0-_array.size
                                        # -- End function
	.globl	string.length                   # -- Begin function string.length
	.p2align	1
	.type	string.length,@function
string.length:                          # @string.length
# %bb.0:
	tail	strlen
.Lfunc_end1:
	.size	string.length, .Lfunc_end1-string.length
                                        # -- End function
	.globl	string.substring                # -- Begin function string.substring
	.p2align	1
	.type	string.substring,@function
string.substring:                       # @string.substring
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	sw	s2, 16(sp)                      # 4-byte Folded Spill
	sw	s3, 12(sp)                      # 4-byte Folded Spill
	mv	s3, a1
	mv	s2, a0
	sub	s1, a2, a1
	addi	a0, s1, 1
	call	malloc
	mv	s0, a0
	add	a1, s2, s3
	mv	a2, s1
	call	memcpy
	add	s1, s1, s0
	sb	zero, 0(s1)
	mv	a0, s0
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	lw	s2, 16(sp)                      # 4-byte Folded Reload
	lw	s3, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end2:
	.size	string.substring, .Lfunc_end2-string.substring
                                        # -- End function
	.globl	string.parseInt                 # -- Begin function string.parseInt
	.p2align	1
	.type	string.parseInt,@function
string.parseInt:                        # @string.parseInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	addi	a2, sp, 8
	call	sscanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end3:
	.size	string.parseInt, .Lfunc_end3-string.parseInt
                                        # -- End function
	.globl	string.ord                      # -- Begin function string.ord
	.p2align	1
	.type	string.ord,@function
string.ord:                             # @string.ord
# %bb.0:
	add	a0, a0, a1
	lbu	a0, 0(a0)
	ret
.Lfunc_end4:
	.size	string.ord, .Lfunc_end4-string.ord
                                        # -- End function
	.globl	print                           # -- Begin function print
	.p2align	1
	.type	print,@function
print:                                  # @print
# %bb.0:
	lui	a1, %hi(.L.str.1)
	addi	a1, a1, %lo(.L.str.1)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end5:
	.size	print, .Lfunc_end5-print
                                        # -- End function
	.globl	println                         # -- Begin function println
	.p2align	1
	.type	println,@function
println:                                # @println
# %bb.0:
	tail	puts
.Lfunc_end6:
	.size	println, .Lfunc_end6-println
                                        # -- End function
	.globl	printInt                        # -- Begin function printInt
	.p2align	1
	.type	printInt,@function
printInt:                               # @printInt
# %bb.0:
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end7:
	.size	printInt, .Lfunc_end7-printInt
                                        # -- End function
	.globl	printlnInt                      # -- Begin function printlnInt
	.p2align	1
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
# %bb.0:
	lui	a1, %hi(.L.str.3)
	addi	a1, a1, %lo(.L.str.3)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end8:
	.size	printlnInt, .Lfunc_end8-printlnInt
                                        # -- End function
	.globl	getString                       # -- Begin function getString
	.p2align	1
	.type	getString,@function
getString:                              # @getString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	li	a0, 1024
	call	malloc
	mv	s0, a0
	lui	a0, %hi(.L.str.1)
	addi	a0, a0, %lo(.L.str.1)
	mv	a1, s0
	call	scanf
	mv	a0, s0
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end9:
	.size	getString, .Lfunc_end9-getString
                                        # -- End function
	.globl	getInt                          # -- Begin function getInt
	.p2align	1
	.type	getInt,@function
getInt:                                 # @getInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	addi	a1, sp, 8
	call	scanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end10:
	.size	getInt, .Lfunc_end10-getInt
                                        # -- End function
	.globl	toString                        # -- Begin function toString
	.p2align	1
	.type	toString,@function
toString:                               # @toString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	mv	s0, a0
	li	a0, 12
	call	malloc
	mv	s1, a0
	lui	a0, %hi(.L.str)
	addi	a1, a0, %lo(.L.str)
	mv	a0, s1
	mv	a2, s0
	call	sprintf
	mv	a0, s1
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end11:
	.size	toString, .Lfunc_end11-toString
                                        # -- End function
	.globl	_toStringBool                   # -- Begin function _toStringBool
	.p2align	1
	.type	_toStringBool,@function
_toStringBool:                          # @_toStringBool
# %bb.0:
	bnez	a0, .LBB12_2
# %bb.1:
	lui	a0, %hi(.L.str.5)
	addi	a0, a0, %lo(.L.str.5)
	ret
.LBB12_2:
	lui	a0, %hi(.L.str.4)
	addi	a0, a0, %lo(.L.str.4)
	ret
.Lfunc_end12:
	.size	_toStringBool, .Lfunc_end12-_toStringBool
                                        # -- End function
	.globl	_mallocClass                    # -- Begin function _mallocClass
	.p2align	1
	.type	_mallocClass,@function
_mallocClass:                           # @_mallocClass
# %bb.0:
	tail	malloc
.Lfunc_end13:
	.size	_mallocClass, .Lfunc_end13-_mallocClass
                                        # -- End function
	.globl	_mallocArray                    # -- Begin function _mallocArray
	.p2align	1
	.type	_mallocArray,@function
_mallocArray:                           # @_mallocArray
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	mv	s0, a1
	mul	a0, a1, a0
	addi	a0, a0, 4
	call	malloc
	addi	a1, a0, 4
	sw	s0, 0(a0)
	mv	a0, a1
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end14:
	.size	_mallocArray, .Lfunc_end14-_mallocArray
                                        # -- End function
	.globl	_compareString                  # -- Begin function _compareString
	.p2align	1
	.type	_compareString,@function
_compareString:                         # @_compareString
# %bb.0:
	tail	strcmp
.Lfunc_end15:
	.size	_compareString, .Lfunc_end15-_compareString
                                        # -- End function
	.globl	_concatString                   # -- Begin function _concatString
	.p2align	1
	.type	_concatString,@function
_concatString:                          # @_concatString
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	sw	s2, 16(sp)                      # 4-byte Folded Spill
	sw	s3, 12(sp)                      # 4-byte Folded Spill
	sw	s4, 8(sp)                       # 4-byte Folded Spill
	sw	s5, 4(sp)                       # 4-byte Folded Spill
	mv	s2, a1
	mv	s3, a0
	call	strlen
	mv	s0, a0
	mv	a0, s2
	call	strlen
	mv	s4, a0
	add	s5, a0, s0
	addi	a0, s5, 1
	call	malloc
	mv	s1, a0
	mv	a1, s3
	mv	a2, s0
	call	memcpy
	add	a0, s1, s0
	mv	a1, s2
	mv	a2, s4
	call	memcpy
	add	s5, s5, s1
	sb	zero, 0(s5)
	mv	a0, s1
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	lw	s2, 16(sp)                      # 4-byte Folded Reload
	lw	s3, 12(sp)                      # 4-byte Folded Reload
	lw	s4, 8(sp)                       # 4-byte Folded Reload
	lw	s5, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end16:
	.size	_concatString, .Lfunc_end16-_concatString
                                        # -- End function
	.globl	_concatStringMulti              # -- Begin function _concatStringMulti
	.p2align	1
	.type	_concatStringMulti,@function
_concatStringMulti:                     # @_concatStringMulti
# %bb.0:
	addi	sp, sp, -80
	sw	ra, 44(sp)                      # 4-byte Folded Spill
	sw	s0, 40(sp)                      # 4-byte Folded Spill
	sw	s1, 36(sp)                      # 4-byte Folded Spill
	sw	s2, 32(sp)                      # 4-byte Folded Spill
	sw	s3, 28(sp)                      # 4-byte Folded Spill
	sw	s4, 24(sp)                      # 4-byte Folded Spill
	sw	s5, 20(sp)                      # 4-byte Folded Spill
	sw	s6, 16(sp)                      # 4-byte Folded Spill
	mv	s6, a0
	sw	a7, 76(sp)
	sw	a6, 72(sp)
	sw	a5, 68(sp)
	sw	a4, 64(sp)
	sw	a3, 60(sp)
	sw	a2, 56(sp)
	sw	a1, 52(sp)
	addi	s4, sp, 52
	sw	s4, 12(sp)
	slli	s5, a0, 2
	beqz	a0, .LBB17_3
# %bb.1:
	lw	s1, 12(sp)
	li	s3, 0
	addi	s1, s1, 4
	add	s0, s1, s5
.LBB17_2:                               # =>This Inner Loop Header: Depth=1
	sw	s1, 12(sp)
	lw	a0, -4(s1)
	call	strlen
	addi	s1, s1, 4
	add	s3, s3, a0
	bne	s1, s0, .LBB17_2
	j	.LBB17_4
.LBB17_3:
	li	s3, 0
.LBB17_4:                               # %.loopexit1
	addi	a0, s3, 1
	call	malloc
	mv	s2, a0
	sw	s4, 12(sp)
	beqz	s6, .LBB17_7
# %bb.5:
	lw	s0, 12(sp)
	li	s1, 0
	addi	s0, s0, 4
	add	s6, s0, s5
.LBB17_6:                               # =>This Inner Loop Header: Depth=1
	sw	s0, 12(sp)
	lw	s5, -4(s0)
	mv	a0, s5
	call	strlen
	mv	s4, a0
	add	a0, s2, s1
	mv	a1, s5
	mv	a2, s4
	call	memcpy
	addi	s0, s0, 4
	add	s1, s1, s4
	bne	s0, s6, .LBB17_6
.LBB17_7:                               # %.loopexit
	add	s3, s3, s2
	sb	zero, 0(s3)
	mv	a0, s2
	lw	ra, 44(sp)                      # 4-byte Folded Reload
	lw	s0, 40(sp)                      # 4-byte Folded Reload
	lw	s1, 36(sp)                      # 4-byte Folded Reload
	lw	s2, 32(sp)                      # 4-byte Folded Reload
	lw	s3, 28(sp)                      # 4-byte Folded Reload
	lw	s4, 24(sp)                      # 4-byte Folded Reload
	lw	s5, 20(sp)                      # 4-byte Folded Reload
	lw	s6, 16(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 80
	ret
.Lfunc_end17:
	.size	_concatStringMulti, .Lfunc_end17-_concatStringMulti
                                        # -- End function
	.globl	v_mallocArrayMulti              # -- Begin function v_mallocArrayMulti
	.p2align	1
	.type	v_mallocArrayMulti,@function
v_mallocArrayMulti:                     # @v_mallocArrayMulti
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	sw	s2, 16(sp)                      # 4-byte Folded Spill
	sw	s3, 12(sp)                      # 4-byte Folded Spill
	sw	s4, 8(sp)                       # 4-byte Folded Spill
	sw	s5, 4(sp)                       # 4-byte Folded Spill
	sw	s6, 0(sp)                       # 4-byte Folded Spill
	lw	s1, 0(a3)
	li	s4, 1
	mv	s3, a0
	bne	a1, s4, .LBB18_2
# %bb.1:
	mul	a0, s1, s3
	addi	a0, a0, 4
	call	malloc
	sw	s1, 0(a0)
	addi	a0, a0, 4
	j	.LBB18_7
.LBB18_2:
	mv	s2, a2
	mv	s0, a1
	mv	s5, a3
	slli	a0, s1, 2
	addi	a0, a0, 4
	call	malloc
	sw	s1, 0(a0)
	addi	a0, a0, 4
	beq	s2, s4, .LBB18_7
# %bb.3:
	beqz	s1, .LBB18_7
# %bb.4:
	addi	s4, s5, 4
	addi	s6, s0, -1
	addi	s2, s2, -1
	mv	s5, a0
	mv	s0, a0
.LBB18_5:                               # =>This Inner Loop Header: Depth=1
	mv	a0, s3
	mv	a1, s6
	mv	a2, s2
	mv	a3, s4
	call	v_mallocArrayMulti
	sw	a0, 0(s0)
	addi	s1, s1, -1
	addi	s0, s0, 4
	bnez	s1, .LBB18_5
# %bb.6:
	mv	a0, s5
.LBB18_7:                               # %.loopexit
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	lw	s2, 16(sp)                      # 4-byte Folded Reload
	lw	s3, 12(sp)                      # 4-byte Folded Reload
	lw	s4, 8(sp)                       # 4-byte Folded Reload
	lw	s5, 4(sp)                       # 4-byte Folded Reload
	lw	s6, 0(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end18:
	.size	v_mallocArrayMulti, .Lfunc_end18-v_mallocArrayMulti
                                        # -- End function
	.globl	_mallocArrayMulti               # -- Begin function _mallocArrayMulti
	.p2align	1
	.type	_mallocArrayMulti,@function
_mallocArrayMulti:                      # @_mallocArrayMulti
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 4(sp)                       # 4-byte Folded Spill
	sw	a7, 28(sp)
	sw	a6, 24(sp)
	sw	a5, 20(sp)
	sw	a4, 16(sp)
	sw	a3, 12(sp)
	addi	a3, sp, 12
	sw	a3, 0(sp)
	call	v_mallocArrayMulti
	lw	ra, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end19:
	.size	_mallocArrayMulti, .Lfunc_end19-_mallocArrayMulti
                                        # -- End function
	.type	.L.str,@object                  # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%d"
	.size	.L.str, 3

	.type	.L.str.1,@object                # @.str.1
.L.str.1:
	.asciz	"%s"
	.size	.L.str.1, 3

	.type	.L.str.3,@object                # @.str.3
.L.str.3:
	.asciz	"%d\n"
	.size	.L.str.3, 4

	.type	.L.str.4,@object                # @.str.4
.L.str.4:
	.asciz	"true"
	.size	.L.str.4, 5

	.type	.L.str.5,@object                # @.str.5
.L.str.5:
	.asciz	"false"
	.size	.L.str.5, 6

	.ident	"clang version 18.1.8"
	.section	".note.GNU-stack","",@progbits
	.addrsig
