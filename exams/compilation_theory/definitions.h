//
// Created by Haotian Bai on 6/26/2020.
//

#ifndef UNTITLED_DEFINITIONS_H
#define UNTITLED_DEFINITIONS_H

#include <vector>
#include <set>
#include <map>
#include <stack>
#define BUF 50
using namespace std;


vector<pair<string, vector<string>>> gram;
set<string> terms{"IF", "THEN", "+", "-", "*", "\\", ":="};
set<string> nonTerms;
map<string,set<string>> firsts;
void find_first(const string& non_term);

void find_follow(vector <pair<char, string>> gram,
                 map<char, set<char> > &follows,
                 map<char, set<char> > firsts,
                 char non_term);

#endif //UNTITLED_DEFINITIONS_H
