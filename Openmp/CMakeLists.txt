 cmake_minimum_required(VERSION 2.8.4)
 project(Openmp)

 set(CMAKE_CXX_STANDARD 20)
 set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fconcepts -fopenmp")
# BOOST
 set(CMAKE_INCLUDE_PATH ${CMAKE_INCLUDE_PATH}  "D:/environment/programming/c++/boost/boost_1_72_0")
 set(CMAKE_LIBRARY_PATH ${CMAKE_LIBRARY_PATH} "D:/environment/programming/c++/boost/boost_1_72_0/libs")
 find_package(Boost)
 IF (Boost_FOUND)
  INCLUDE_DIRECTORIES(${Boost_INCLUDE_DIR})
  ADD_DEFINITIONS( "-DHAS_BOOST" )
 ENDIF()

 add_executable(Openmp main.cpp)
 target_link_libraries( Openmp ${Boost_LIBRARIES} )