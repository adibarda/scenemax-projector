package com.scenemaxng.projector;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import java.util.ArrayList;
import java.util.HashMap;

public class SceneMaxApp extends com.jme3.app.SimpleApplication implements IUiProxy {

    private static HashMap<String, AppModel> models = new HashMap<String, AppModel>();
    private IAppObserver _appObserver = null;
    private ArrayList<ISceneMaxController> _controllers = new ArrayList<>();
    private UITerrainHandler terrainHandler = null;


    public SceneMaxApp() {

    }

    public SceneMaxApp(IAppObserver observer) {
        _appObserver=observer;
    }

    public void setObserver(IAppObserver observer) {
        _appObserver=observer;
        _appObserver.init();
    }

    @Override
    public void simpleInitApp() {

        if(_appObserver!=null) {
            _appObserver.init();
        }

        //this.setShowSettings(false);
        this.setDisplayFps(false);
        this.setDisplayStatView(false);

        this.flyCam.setEnabled(false); // eliminates the mouse binding to the camera

        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        rootNode.addLight(sun);

//        terrainHandler = new UITerrainHandler();
//        terrainHandler.assetManager=this.assetManager;
//        terrainHandler.rootNode = this.rootNode;
//        terrainHandler.camera = this.getCamera();
//
//        terrainHandler.init();
    }

    public void run(String code) {
        final String result = new SceneMaxLanguageParser(this).parse(code);
    }

    @Override
    public int loadModel(String name, String resourcePath) {

        // Load a model from test_data (OgreXML + material + texture)
        Spatial model = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        models.put(name,new AppModel(model));
        model.scale(0.03f, 0.03f, 0.03f);
        model.rotate(0.0f, -3.0f, 0.0f);
        model.setLocalTranslation(0.0f, -5.0f, -2.0f);
        rootNode.attachChild(model);
        return 0;
    }

    @Override
    public int loadSprite(String name, String resourcePath) {
        return 0;
    }

    @Override
    public int registerController(ISceneMaxController c) {

        c.setUIProxy(this);
        c.init();
        _controllers.add(c);
        return 0;
    }

    @Override
    public void rotateModel(String targetVar, int axisNum, float direction, float rotateVal) {
        Spatial m = models.get(targetVar).model;

        if(axisNum==1) {
            m.rotate(rotateVal * direction * FastMath.DEG_TO_RAD, 0, 0);
        } else if(axisNum==2) {
            m.rotate(0, rotateVal * direction * FastMath.DEG_TO_RAD, 0);
        } else if(axisNum==3) {
            m.rotate(0, 0, rotateVal * direction * FastMath.DEG_TO_RAD);
        }

    }

    @Override
    public void animateModel(String targetVar, String animationName, String speed, AppModelAnimationController controller) {

        AppModel m = models.get(targetVar);
        controller.animate(m, animationName, speed);

    }

    @Override
    public void simpleUpdate(float tpf) {

        for(int i=_controllers.size()-1;i>=0;--i) {
            boolean finished = _controllers.get(i).run(tpf);
            if(finished) {
                _controllers.remove(i);
            }
        }
    }


}
