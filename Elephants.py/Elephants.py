from tokenize import tokenize, Token
import sys


class OperationOnElephants:

    error = False
    inputData = None
    numberOfLines = 4
    lines = []
    weight = []
    firstStructure = []
    secondStructure = []
    numberOfElephants = None
    numberOfCycles = 0
    globalMinWeight = 0
    cycleList = []
    weightSumInCycleList = []
    cycleWeightMin = []

    def __init__(self, inputData):
        self.inputData = inputData
        self.readLineFile()

    def readLineFile(self):
        for i in range(0, self.numberOfLines):
            line = self.inputData.readline()
            line = line.strip()
            if line:
                self.lines.append(line)
            else:
                self.error = True
                return

    def printResult(self):
        self.setNumberOfElephants()

        if self.numberOfElephants != 1 and self.error is False:
            result = self.getResult()
            if result != 0 and self.error is False:
                print(result)

    def setNumberOfElephants(self):
        try:
            self.numberOfElephants = int(self.lines[0])
            if self.numberOfElephants > 0:
                self.error = False
                return
        except ValueError:
            pass
        self.error = True

    def getResult(self):
        result = 0
        if self.checkStateOfPreparingData():
            for cycle in range(0, self.numberOfCycles):
                firstMethod = int(self.weightSumInCycleList[cycle]) + \
                              (len(self.cycleList[cycle]) - 2) * int(self.cycleWeightMin[cycle])

                secondMethod = int(self.weightSumInCycleList[cycle]) + \
                               int(self.cycleWeightMin[cycle]) + (len(self.cycleList[cycle]) + 1) * \
                               int(self.globalMinWeight)

                result = result + min(firstMethod, secondMethod)
            return result
        else:
            self.error = True
            return result -1

    def checkStateOfPreparingData(self):
        if self.error is False:
            self.convertStringLineToLists()
            if self.error is False:
                self.createCycle()
                if self.error is False:
                    self.setCyclesParameters()
                    return True

        self.error = True
        return False

    def convertStringLineToLists(self):
        try:
            self.weight = [0] + [int(liczba) for liczba in self.lines[1].split(" ")]
            self.firstStructure = [0] + [int(liczba) for liczba in self.lines[2].split(" ")]
            self.secondStructure = [0] + [int(liczba) for liczba in self.lines[3].split(" ")]

            for position in range(1, self.numberOfElephants):
                if int(self.weight[position]) <= 0 | int(self.firstStructure[position]) <= 0 | int(self.secondStructure[position]) <= 0:
                    self.error = True
                    return
        except ValueError:
            self.error = True
            return

    def createCycle(self):
        decidedArray = self.getDecidedArray()
        for decidedElem in range (1, self.numberOfElephants + 1):
            if decidedArray[decidedElem] is False:
                self.numberOfCycles = self.numberOfCycles + 1
                cycleElem = decidedElem
                self.cycleList.append([])

                self.addElemToCycle(decidedArray, cycleElem)

    def getDecidedArray(self):
        decidedArray = {}
        for elem in range(1, self.numberOfElephants +1):
            decidedArray[elem] = False
        return decidedArray

    def addElemToCycle(self, decidedArray, cycleElem):
        while decidedArray[cycleElem] is False:
            decidedArray[cycleElem] = True
            self.cycleList[self.numberOfCycles - 1].append(cycleElem)
            cycleElem = self.getPermutation()[cycleElem]
            if cycleElem == -1:
                self.error = True
                return

    def getPermutation(self):
        if self.firstStructure[1] != -1 and self.secondStructure[1] != -1:
            permutation = [None]* (self.numberOfElephants + 1)
            for numberOfElephant in range(1, self.numberOfElephants + 1):
                if int(self.secondStructure[numberOfElephant]) <= self.numberOfElephants:
                    permutation[int(self.secondStructure[numberOfElephant])] = int(self.firstStructure[numberOfElephant])
                else:
                    permutation.append(-1)
                    self.error = True
                    return permutation
            return permutation
        else:
            permutation = []
            permutation.append(-1)
            self.error = True
            return permutation

    def setCyclesParameters(self):
        for cycle in range(0, self.numberOfCycles):
            sum = 0
            cycleMinWeight = 0

            for elem in range(0, len(self.cycleList[cycle])):
                sum = sum + int(self.weight[self.cycleList[cycle][elem]])

                if cycleMinWeight != 0:
                    cycleMinWeight = min(cycleMinWeight, self.weight[self.cycleList[cycle][elem]])
                else:
                    cycleMinWeight = self.weight[self.cycleList[cycle][elem]]
            self.weightSumInCycleList.append(sum)
            self.cycleWeightMin.append(cycleMinWeight)

            if self.globalMinWeight != 0:
                self.globalMinWeight = min(cycleMinWeight, self.globalMinWeight)
            else:
                self.globalMinWeight = cycleMinWeight

def main():
    inputData = sys.stdin
    operationOnElephants = OperationOnElephants(inputData)

    operationOnElephants.printResult()


if __name__ == '__main__':
    main()