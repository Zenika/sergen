/*{
  'name':'mathService',
  'type':'business',
  'version':'0.8',
  'description':'Librairies de fonctions mathematiques',
  'functions' :[
    {
      'name':'add',
      'inputs':['int', 'int'],
      'output':'int'
    },
    {
      'name':'sub',
      'inputs':['int', 'int'],
      'output':'int'
    }
  ]
}*/

import com.zenika.sergen.components.SG_COMPONENT_TYPE;
import com.zenika.sergen.components.pojo.SGComponentMethod;

import java.util.ArrayList;

public class MathService {
    public String name = "mathService";
    public SG_COMPONENT_TYPE type = SG_COMPONENT_TYPE.BUSINESS;
    public String description;
    public String version = "0.8";
    public ArrayList<SGComponentMethod> functions;
}