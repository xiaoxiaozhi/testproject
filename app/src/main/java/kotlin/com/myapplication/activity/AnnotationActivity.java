package kotlin.com.myapplication.activity;
//一个用来处理Element的工具类，源代码的每一个部分都是一个特定类型的Element，例如：

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;//packageElement
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kotlin.com.myapplication.R;
import test.com.myprocess.MyButterknife;
//TODO 为什么写在java-library里面的类不能再这里自动补全，// 跟butterknife不太一样

public class AnnotationActivity extends AppCompatActivity {//TypeElement
    private int a;      // VariableElement
    @MyButterknife(R.id.tt)
    TextView aa;

    @BindView(R.id.dd)
    TextView dd;
    Unbinder ub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//ExecuteableElement
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        int b;//VariableElement
        aa.setText("123456789");
        dd.setText("5556667788");
        ub = ButterKnife.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ub.unbind();
    }
}
