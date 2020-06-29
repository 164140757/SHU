#include "definitions.h"

#include <iostream>
#include <fstream>
#include <sstream>
#include <iterator>


void initFirst();

void parseTest(string path);

int main() {
    cout << "Reading grammar files..." << endl;
    string grammarFile = "/mnt/d/Development/SHU/exams/compilation_theory/resources/Grammar_test.txt";
    ifstream grammar_file(grammarFile);
    if (grammar_file.fail()) {
        std::cout << "Error in opening grammar file\n";
        return 1;
    }

    char buffer[BUF];

    while (grammar_file.getline(buffer, BUF, ';')) {
        // buffer to store identifiers
        istringstream iss(buffer);
        // no whitespaces
        vector<string> tokens{istream_iterator<string>{iss}, istream_iterator<string>{}};
        // non terminals
        nonTerms.insert(tokens[0]);
        vector<string> rhs;
        for (int i = 2; i < tokens.size(); ++i) {
            if (tokens[i] != "|") {
                rhs.emplace_back(tokens[i]);
            }
        }
        gram.emplace_back(tokens[0], rhs);
    }
    cout << "Dear professors, please input your test file location:" << endl;
    string testPath;
    cin >> testPath;
    parseTest(testPath);
//    for(const auto & nonTerm : nonTerms) {
//        if(firsts[nonTerm].empty()){
//            find_first(nonTerm);
//        }
//    }
//
//    cout<<"Firsts list: \n";

}

void parseTest(string path) {
    ifstream tIf(path);

}

void initFirst() {

}

void readGrammar(){

}

void find_first(const string& token) {
    for(auto & it : gram) {
        if(it.first != token) continue;
        for(const auto& t:it.second){
            if(terms.contains(t)){
                firsts[token].insert(t);
                break;
            }
            // non terminals for t
            else{
                if(firsts[t].empty()){
                    find_first(t);
                }
                firsts[token].insert(firsts[t].begin(),firsts[t].end());
            }
        }
    }

}