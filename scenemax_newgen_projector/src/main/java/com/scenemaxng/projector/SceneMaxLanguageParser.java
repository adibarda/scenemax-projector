package com.scenemaxng.projector;

import com.abware.scenemaxlang.parser.SceneMaxBaseVisitor;
import com.abware.scenemaxlang.parser.SceneMaxLexer;
import com.abware.scenemaxlang.parser.SceneMaxParser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


public class SceneMaxLanguageParser implements IParser{

    private static HashMap<String, ModelDef> models = new HashMap<String, ModelDef>();
    private static HashMap<String, String> sprites = new HashMap<String, String>();
    private static HashMap<String, String> vars = new HashMap<String, String>();
    private static IUiProxy _uiProxy = null;

    public SceneMaxLanguageParser(IUiProxy proxy) {
        _uiProxy=proxy;
    }

    public String parse(String code) {
        CharStream charStream = new ANTLRInputStream(code);
        SceneMaxLexer lexer = new SceneMaxLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        SceneMaxParser parser = new SceneMaxParser(tokens);

        ProgramVisitor v = new ProgramVisitor();
        v.visit(parser.prog());
        _uiProxy.registerController(v.compositeController);

        return "";
    }



    private static class ProgramVisitor extends SceneMaxBaseVisitor<String> {

        public CompositeController compositeController = null;

        @Override
        public String visitActionStatement(SceneMaxParser.ActionStatementContext ctx) {
            boolean isAsync = ctx.action_statement().async_expr().size()>1;
            if(!isAsync) {
                if(compositeController==null) {
                    compositeController = new CompositeController();
                }
            } else {
                if(compositeController!=null){
                    _uiProxy.registerController(compositeController);
                }

                compositeController=null;
            }
            return this.visitChildren(ctx);
        }

        @Override
        public String visitDefine_variable(SceneMaxParser.Define_variableContext ctx) {

            String varName = ctx.var_decl().ID().getText();
            String resName = ctx.res_var_decl().ID().getText();

            ModelDef md = models.get(resName);
            if(md==null) {
                System.err.println(varName+" cannot use resource "+resName+" because this resource is not defined yet");
                return null;
            }

            _uiProxy.loadModel(varName,md.from);
            vars.put(varName,resName);
            System.out.println("var="+varName+", resource="+resName);

            return null;
        }

        //
        @Override
        public String visitDefModel(SceneMaxParser.DefModelContext ctx) {

            String var = ctx.define_model().res_var_decl().ID().getText();
            String from = ctx.define_model().file_var_decl().getText();
            ModelDef md = new ModelDef();
            md.name=var;
            md.from=from;
            models.put(var, md);
            System.out.println("res="+var+", from="+from);
            return null;
        }


        @Override
        public String visitRotate(SceneMaxParser.RotateContext ctx) {

            String var = ctx.var_decl().ID().getText();

            System.out.println("Going to rotate "+var);
            String speed = ctx.speed_expr().number().getText();

            for(int i=0;i<ctx.axis_expr().size();++i) {
                SceneMaxParser.Axis_exprContext actx =  ctx.axis_expr().get(i);
                String axis = actx.axis_id().getText();
                String numSign = actx.number_sign().getText();
                String num = actx.number().getText();

                System.out.println("\tOn  "+axis+" "+numSign+num+" angles");

                RotateController rc = new RotateController();
                rc.targetVar = var;
                rc.axis = axis;
                rc.numSign = numSign;
                rc.num = num;
                rc.speed=speed;

                if(compositeController!=null){
                    compositeController.add(rc);
                } else {
                    _uiProxy.registerController(rc);
                }

            }
            return null;
        }

        @Override
        public String visitAnimate(SceneMaxParser.AnimateContext ctx) {

            String var = ctx.var_decl().ID().getText();
            boolean firstAnim=true;

            CompositeController cc = new CompositeController();

            for(int i=0;i<ctx.anim_expr().size();++i) {
                SceneMaxParser.Anim_exprContext actx =  ctx.anim_expr().get(i);
                String speed = actx.speed_of_expr().number().getText();
                String anim = actx.animation_name().getText();
                if(firstAnim) {
                    firstAnim=false;
                    System.out.println("Going to animate " + var + " " + anim + " in " + speed + " seconds");
                } else {
                    System.out.println("    Then animate " + var + " " + anim + " in " + speed + " seconds");
                }

                ModelAnimateController ac = new ModelAnimateController();
                ac.animationName = anim;
                ac.targetVar = var;
                ac.speed=speed;
                cc.add(ac);
            }

            if(compositeController!=null){
                compositeController.add(cc);
            } else {
                _uiProxy.registerController(cc);
            }

            return null;
        }

    }


}
