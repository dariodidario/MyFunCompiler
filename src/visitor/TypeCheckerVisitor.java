package visitor;

import nodetype.CompositeNodeType;
import nodetype.FunctionNodeType;
import nodetype.NodeType;
import nodetype.PrimitiveNodeType;
import org.w3c.dom.Node;
import semantic.SymbolTable;
import syntax.*;
import syntax.expr.*;
import syntax.expr.binaryexpr.arithop.*;
import syntax.expr.binaryexpr.relop.*;
import syntax.expr.binaryexpr.stringop.StringConcatOp;
import syntax.expr.unaryexpr.MinusExpr;
import syntax.expr.unaryexpr.NotExpr;
import syntax.expr.unaryexpr.OutParIdExpr;
import syntax.expr.unaryexpr.ParExpr;
import syntax.statement.*;
import syntax.statement.WriteOps.WriteBOp;
import syntax.statement.WriteOps.WriteLnOp;
import syntax.statement.WriteOps.WriteOp;
import syntax.statement.WriteOps.WriteTOp;
import syntax.type.PrimitiveType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TypeCheckerVisitor implements Visitor <NodeType, SymbolTable> {


    private Consumer<? super AstNode> typeCheck(SymbolTable arg){
        return (AstNode node) -> node.accept(this, arg);
    }

    @Override
    public NodeType visit(Program program, SymbolTable arg) {
        arg.enterScope();
        if(program.getVarDeclList() != null){
            program.getVarDeclList().forEach(this.typeCheck(arg));
        }
        if(program.getFunList() != null){
            program.getFunList().forEach(this.typeCheck(arg));
        }
        if (program.getMain()!= null){
            program.getMain().accept(this,arg);
        }
        arg.exitScope();
        return PrimitiveNodeType.NULL;

    }

    @Override
    public NodeType visit(PlusOp plusOp, SymbolTable arg) {
        NodeType leftOperandType = plusOp.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = plusOp.getRightOperand().accept(this,arg);
        NodeType result = leftOperandType.checkAdd((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            plusOp.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(MinusOp minusOp, SymbolTable arg) {
        NodeType leftOperandType = minusOp.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = minusOp.getRightOperand().accept(this,arg);
        NodeType result = leftOperandType.checkSub((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            minusOp.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(TimesOp timesOp, SymbolTable arg) {
        NodeType leftOperandType = timesOp.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = timesOp.getRightOperand().accept(this,arg);
        NodeType result = leftOperandType.checkMul((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            timesOp.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(DivOp divOp, SymbolTable arg) {
        NodeType leftOperandType = divOp.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = divOp.getRightOperand().accept(this,arg);
        NodeType result = leftOperandType.checkDiv((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            divOp.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(DivIntOp divIntOp, SymbolTable arg) {
        NodeType leftOperandType = divIntOp.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = divIntOp.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkDivInt((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            divIntOp.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(PowOp powOp, SymbolTable arg) {
        NodeType baseOperandType = powOp.getBaseOperand().accept(this, arg);
        NodeType exponentOperandType = powOp.getExponentOperand().accept(this,arg);
        NodeType result = baseOperandType.checkPow((PrimitiveNodeType) exponentOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ baseOperandType.toString()+" to " +exponentOperandType.toString());
        }else{
            powOp.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(StringConcatOp stringConcatOp, SymbolTable arg) {
        NodeType leftOperandType = stringConcatOp.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = stringConcatOp.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkStrConcat((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            stringConcatOp.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(AndRelop andRelop, SymbolTable arg) {
        NodeType leftOperandType = andRelop.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = andRelop.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkRel((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            andRelop.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(OrRelop orRelop, SymbolTable arg) {
        NodeType leftOperandType = orRelop.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = orRelop.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkRel((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            orRelop.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(NotEqualRelop notEqualRelop, SymbolTable arg) {
        NodeType leftOperandType = notEqualRelop.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = notEqualRelop.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkRel((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
           notEqualRelop.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(EqualRelop equalRelop, SymbolTable arg) {
        NodeType leftOperandType = equalRelop.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = equalRelop.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkRel((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            equalRelop.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(GreaterThanRelop greaterThanRelop, SymbolTable arg) {
        NodeType leftOperandType = greaterThanRelop.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = greaterThanRelop.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkRel((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            greaterThanRelop.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(GreaterEqualRelop greaterEqualRelop, SymbolTable arg) {
        NodeType leftOperandType = greaterEqualRelop.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = greaterEqualRelop.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkRel((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            greaterEqualRelop.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(LessThanRelop lessThanRelop, SymbolTable arg) {
        NodeType leftOperandType = lessThanRelop.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = lessThanRelop.getRightOperand().accept(this,arg);

        NodeType result = leftOperandType.checkRel((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            lessThanRelop.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(LessEqualRelop lessEqualRelop, SymbolTable arg) {
        NodeType leftOperandType = lessEqualRelop.getLeftOperand().accept(this, arg);
        NodeType rightOperandType = lessEqualRelop.getRightOperand().accept(this,arg);
        NodeType result = leftOperandType.checkRel((PrimitiveNodeType) rightOperandType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from"+ leftOperandType.toString()+" to " +rightOperandType.toString());
        }else{
            lessEqualRelop.setNodeType((PrimitiveNodeType) result);
            return result;
        }
    }

    @Override
    public NodeType visit(BoolConst boolConst, SymbolTable arg) {
        boolConst.setNodeType(PrimitiveNodeType.BOOL);
        return PrimitiveNodeType.BOOL;

    }

    @Override
    public NodeType visit(IntegerConst integerConst, SymbolTable arg) {
        integerConst.setNodeType((PrimitiveNodeType.INT));
        return PrimitiveNodeType.INT;
    }

    @Override
    public NodeType visit(RealConst realConst, SymbolTable arg) {
        realConst.setNodeType(PrimitiveNodeType.REAL);
        return PrimitiveNodeType.REAL;
    }

    @Override
    public NodeType visit(StringConst stringConst, SymbolTable arg) {
        stringConst.setNodeType(PrimitiveNodeType.STRING);
        return PrimitiveNodeType.STRING;
    }

    @Override
    public NodeType visit(Id id, SymbolTable arg) {
        NodeType idNodeType = arg.lookup(id.getValue()).get().getNodeType();
        id.setNodeType(idNodeType);
        return idNodeType;
    }

    @Override
    public NodeType visit(CallFunction callFunction, SymbolTable arg) {
        NodeType idNodeType = arg.lookup(callFunction.getId().getValue()).get().getNodeType();
        CompositeNodeType compositeActualTypes = new CompositeNodeType(new ArrayList<NodeType>());

        for(Expr element : callFunction.getExprList()){
            NodeType t= element.accept(this,arg);
            compositeActualTypes.addNodeType(t);
        }
        if(!(idNodeType instanceof FunctionNodeType)){
            throw new RuntimeException("Kind mismatch:"+callFunction.getId().getValue()+" is not a function!");
        }else{
            FunctionNodeType f =(FunctionNodeType) idNodeType;
            CompositeNodeType paramList = f.getParamsType();
            if(paramList.equals(compositeActualTypes)){
                callFunction.setNodeType(idNodeType);
            }else{
                throw new RuntimeException("Type mismatch:"+callFunction.getId()+" doesn't respect parameters:"+paramList.toString());
            }
        }
        if(callFunction.getExprList() != null){
            callFunction.getExprList().forEach(this.typeCheck(arg));
        }
        return ((FunctionNodeType) idNodeType).getNodeType();
    }

    @Override
    public NodeType visit(MinusExpr minusExpr, SymbolTable arg) {
        NodeType exprNodeType = minusExpr.getExpr().accept(this,arg);
        NodeType appType =  PrimitiveNodeType.INT;
        NodeType result = appType.checkSub((PrimitiveNodeType) exprNodeType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from int"+" to " +exprNodeType.toString());
        }else{
            minusExpr.setNodeType(result);
            return result;
        }

    }

    @Override
    public NodeType visit(NotExpr notExpr, SymbolTable arg) {
        NodeType exprNodeType = notExpr.getExpr().accept(this,arg);
        NodeType appType = PrimitiveNodeType.BOOL;
        NodeType result = appType.checkRel((PrimitiveNodeType) exprNodeType);
        if(result.equals(PrimitiveNodeType.NULL)){
            throw new RuntimeException("Type Expression Mismatch:cannot convert from bool" + " to " + exprNodeType.toString());
        }else{
            notExpr.setNodeType(result);
            return result;
        }
    }

    @Override
    public NodeType visit(ParExpr parExpr, SymbolTable arg) {
        NodeType exprNode;
        if(parExpr.getExpr() != null){
           exprNode = parExpr.getExpr().accept(this, arg);
        }else{
            exprNode = PrimitiveNodeType.NULL;
        }
        return exprNode;

    }

    @Override
    public NodeType visit(OutParIdExpr outParIdExpr, SymbolTable arg) {
        NodeType idNodeType = arg.lookup(outParIdExpr.getId().getValue()).get().getNodeType();
        outParIdExpr.setNodeType(idNodeType);
        return idNodeType;
    }

    @Override
    public NodeType visit(IdInitOp idInitOp, SymbolTable arg) {
        NodeType idNodeType = arg.lookup(idInitOp.getId().getValue()).get().getNodeType();
        NodeType exprNodeType;
        if(idInitOp.getExpr()!=null){
             exprNodeType= idInitOp.getExpr().accept(this,arg);
        }else{
            exprNodeType = PrimitiveNodeType.NULL;
        }
        if(idNodeType.equals(exprNodeType) || exprNodeType.equals(PrimitiveNodeType.NULL)){
            idInitOp.setNodeType((PrimitiveNodeType) idNodeType);
            idInitOp.getId().setNodeType((PrimitiveNodeType) idNodeType);
            return idNodeType;
        }else{
            throw new RuntimeException("Type Mismatch:declaring a variable of type"+ idNodeType.toString()+"but its value is "+exprNodeType.toString());
        }

    }

    @Override
    public NodeType visit(Const constant, SymbolTable arg) {
        return constant.getType().typeFactory();
    }

    @Override
    public NodeType visit(IdInitObblOp idInitObblOp, SymbolTable arg) {

        NodeType result = arg.lookup(idInitObblOp.getId().getValue()).get().getNodeType();
        idInitObblOp.setNodeType((PrimitiveNodeType) result);
        idInitObblOp.getId().setNodeType((PrimitiveNodeType) result);
        return result;
    }

    @Override
    public NodeType visit(PrimitiveType primitiveType, SymbolTable arg) {
        return primitiveType.typeFactory();
    }

    @Override
    public NodeType visit(ParDecl parDecl, SymbolTable arg) {
        NodeType nodetype = parDecl.getType().typeFactory();
        NodeType idNode = arg.lookup(parDecl.getId().getValue()).get().getNodeType();
           if(!nodetype.equals(idNode)){
               throw new RuntimeException("Type Mismatch. " +  parDecl.getId().getValue() + " cannot be " + nodetype.toString());
           }
           return nodetype;
    }

    @Override
    public NodeType visit(ElseOp elseOp, SymbolTable arg) {
        arg.enterScope();
        elseOp.getVarDeclList().forEach(this.typeCheck(arg));
        elseOp.getStatList().forEach(this.typeCheck(arg));
        arg.exitScope();
        return PrimitiveNodeType.NULL;
    }

    @Override
    public NodeType visit(IfStat ifStat, SymbolTable arg) {
        arg.enterScope();
        NodeType condIf = ifStat.getExpr().accept(this, arg);
        NodeType ElseOp = ifStat.getElseOp().accept(this,arg);
        if(!condIf.equals(PrimitiveNodeType.BOOL)) {
            throw new RuntimeException("Type Mismatch. if condition doesn't return boolean");
        }
        ifStat.getStatList().forEach(this.typeCheck(arg));
        arg.exitScope();
        return PrimitiveNodeType.NULL;
    }

    @Override
    public NodeType visit(WhileStat whileStat, SymbolTable arg) {
        arg.enterScope();
        NodeType condWhile = whileStat.getExpr().accept(this, arg);

        if(!condWhile.equals(PrimitiveNodeType.BOOL)) {
            throw new RuntimeException("Type Mismatch. While condition doesn't return boolean");
        }
        whileStat.getStatList().forEach(this.typeCheck(arg));
        arg.exitScope();
        return PrimitiveNodeType.NULL;
    }

    @Override
    public NodeType visit(ReadStat readStat, SymbolTable arg) {
       if(readStat.getExpr()!= null){
           NodeType exprType =  readStat.getExpr().accept(this,arg);
           if(!PrimitiveNodeType.STRING.equals(exprType)){
               throw new RuntimeException("Type Mismatch. Read condition admit only string expression messages!");
           }
       }
        readStat.getIdList().forEach(id -> id.accept(this,arg));
        return PrimitiveNodeType.NULL;
    }

    @Override
    public NodeType visit(AssignStat assignStat, SymbolTable arg) {
        NodeType idType = arg.lookup(assignStat.getId().getValue()).get().getNodeType();
        NodeType exprType = assignStat.getExpr().accept(this,arg);
        if(!idType.equals(exprType)){
            throw new RuntimeException("Type Mismatch. Trying to assign to "+ idType.toString()+ " a " +exprType.toString() +" expression" );
        }else{
            return idType;
        }
    }

    @Override
    public NodeType visit(ReturnOp returnOp, SymbolTable arg) {
        if(returnOp.getExpr() != null) {
            NodeType exprNode = returnOp.getExpr().accept(this, arg);
            return exprNode;
        }
        return PrimitiveNodeType.NULL;
    }

    @Override
    public NodeType visit(WriteOp writeOp, SymbolTable arg) {
        NodeType writeType= writeOp.getExpr().accept(this, arg);
        return writeType;
    }

    @Override
    public NodeType visit(WriteLnOp writeLnOp, SymbolTable arg) {
        NodeType writeType= writeLnOp.getExpr().accept(this, arg);
        return writeType;
    }

    @Override
    public NodeType visit(WriteTOp writeTOp, SymbolTable arg) {
        NodeType writeType= writeTOp.getExpr().accept(this, arg);
        return writeType;
    }

    @Override
    public NodeType visit(WriteBOp writeBOp, SymbolTable arg) {
        NodeType writeType= writeBOp.getExpr().accept(this, arg);
        return writeType;
    }

    @Override
    public NodeType visit(Fun fun, SymbolTable arg) {
        arg.enterScope();
        NodeType idNode = arg.lookup(fun.getId().getValue()).get().getNodeType();
        if (idNode instanceof FunctionNodeType){
            FunctionNodeType f = (FunctionNodeType) idNode;
            fun.getParamDeclList().forEach(parDecl -> parDecl.accept(this,arg));
            fun.getType().accept(this,arg);
            fun.getVarDeclList().forEach(varDecl -> varDecl.accept(this,arg));
            fun.getStatList().forEach(stat -> stat.accept(this,arg));
            arg.exitScope();
            return f.getNodeType();

        }else{
            throw new RuntimeException("Type Mismatch. Cannot typecheck function:"+fun.getId().getValue());
        }


    }

    @Override
    public NodeType visit(Main main, SymbolTable arg) {
        arg.enterScope();
        if(main.getVarDeclList()!= null){
            main.getVarDeclList().forEach(varDecl -> varDecl.accept(this,arg));
        }
        if(main.getStatList() != null){
            main.getStatList().forEach(stat -> stat.accept(this,arg));
        }
        arg.exitScope();
        return PrimitiveNodeType.NULL;
    }

    @Override
    public NodeType visit(CallFunctionStat callFunctionStat, SymbolTable arg) {
        return callFunctionStat.getCallFunction().accept(this,arg);
    }

    @Override
    public NodeType visit(VarDecl varDecl, SymbolTable arg) {
        if(varDecl.getIdListInitOp() != null){
            Type vtype =varDecl.getType();
            NodeType pvtype= vtype.typeFactory();
            for(IdInitOp element : varDecl.getIdListInitOp()){
                NodeType idtype = arg.lookup(element.getId().getValue()).get().getNodeType();
                if(!pvtype.equals(idtype)){
                    throw new RuntimeException("Type Mismatch:declaring a variable of type "+ pvtype.toString()+" but its value is " +idtype.toString());
                }
                element.accept(this,arg);
            }
            return pvtype;
        }
        if(varDecl.getIdListInitObblOp() != null){
            varDecl.getIdListInitObblOp().forEach(this.typeCheck(arg));
            return PrimitiveNodeType.NULL;
        }
        return PrimitiveNodeType.NULL;
    }
}