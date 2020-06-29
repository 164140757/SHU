//
// Created by Haotian Bai on 6/27/2020.
//

#ifndef UNTITLED_PARSER_H
#define UNTITLED_PARSER_H

#include <string>
using namespace std;
class Parser {
public:
    void read(string filePath);
    enum Token_type{NUM,WORD,OPE};
private:
    void parse(string token);
};


#endif //UNTITLED_PARSER_H
