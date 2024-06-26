class Test {
    final char c = 1;
    final int i = 1;

    public void operationsWithChar() {
        b(i > c);
        b(i >= c);
        b(i < c);
        b(i <= c);

        b(c > i);
        b(c >= i);
        b(c < i);
        b(c <= i);

        b(c == i);
        b(c != i);
        b(i == c);
        b(i != c);

        i(i + c);
        i(i - c);
        i(i / c);
        i(i * c);
        i(i % c);
        i(i | c);
        i(i & c);
        i(i ^ c);
        i(i << c);
        i(i >> c);
        i(i >>> c);
        i(/*operand 'i'*/ i /*plus*/ + /*operand 'c'*/ c);
        i(/*operand 'i'*/ i /*xor*/ ^ /*operand 'c'*/ c);
        i(/*operand 'i'*/ i /*shr*/ >>> /*operand 'c'*/ c);

        i(c + i);
        i(c - i);
        i(c / i);
        i(c * i);
        i(c % i);
        i(c | i);
        i(c & i);
        i(c ^ i);
        i(c << i);
        i(c >> i);
        i(c >>> i);

        i(~c);
        i(-c);
        i(+c);
        i(/*operand 'c'*/ ~c);
        i(/*operand 'c'*/ -c);
        i(/*operand 'c'*/ +c);
    }

    public void operationsWithCharLiteral() {
        b('0' > c);
        b('0' >= c);
        b('0' < c);
        b('0' <= c);

        b(c > '0');
        b(c >= '0');
        b(c < '0');
        b(c <= '0');

        b(c == '0');
        b(c != '0');

        b('0' == c);
        b('0' != c);

        b(i > '0');
        b(i >= '0');
        b(i < '0');
        b(i <= '0');

        b('0' > i);
        b('0' >= i);
        b('0' < i);
        b('0' <= i);

        b('0' == i);
        b('0' != i);

        b(i == '0');
        b(i != '0');

        i(i + '0');
        i(i - '0');
        i(i / '0');
        i(i * '0');
        i(i % '0');
        i(i | '0');
        i(i & '0');
        i(i ^ '0');
        i(i << '0');
        i(i >> '0');
        i(i >>> '0');

        i(c + '0');
        i(c - '0');
        i(c / '0');
        i(c * '0');
        i(c % '0');
        i(c | '0');
        i(c & '0');
        i(c ^ '0');
        i(c << '0');
        i(c >> '0');
        i(c >>> '0');

        i('0' + i);
        i('0' - i);
        i('0' / i);
        i('0' * i);
        i('0' % i);
        i('0' | i);
        i('0' & i);
        i('0' ^ i);
        i('0' << i);
        i('0' >> i);
        i('0' >>> i);

        i('0' + c);
        i('0' - c);
        i('0' / c);
        i('0' * c);
        i('0' % c);
        i('0' | c);
        i('0' & c);
        i('0' ^ c);
        i('0' << c);
        i('0' >> c);
        i('0' >>> c);

        i(~'0');
        i(-'0');
        i(+'0');
    }

    public void operationsWithCharLiterals() {
        b('A' > '0');
        b('A' >= '0');
        b('A' < '0');
        b('A' <= '0');

        b('0' == 'A');
        b('0' != 'A');

        i('A' + '0');
        i('A' - '0');
        i('A' / '0');
        i('A' * '0');
        i('A' % '0');
        i('A' | '0');
        i('A' & '0');
        i('A' ^ '0');
        i('A' << '0');
        i('A' >> '0');
        i('A' >>> '0');
        i(/*operand 'A'*/ 'A' + /*plus*/ /*operand '0'*/ '0');
        i(/*operand 'A'*/ 'A' ^ /*xor*/ /*operand '0'*/ '0');
        i(/*operand 'A'*/ 'A' >>> /*shr*/ /*operand '0'*/ '0');
    }

    public void b(boolean b) {
    }

    public void i(int i) {
    }
}