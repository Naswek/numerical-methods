import { useEffect, useMemo, useState } from "react";
import { getSamples, solveOde } from "../api/solver";
import OdeForm from "../components/OdeForm";
import OdeResultPanel from "../components/OdeResultPanel";
import OdeGraph from "../components/OdeGraph";

function parseX(text) {
  const val = String(text).trim().replace(",", ".");
  return val === "" ? 0 : Number(val);
}

export default function OdePage() {
  const [samples, setSamples] = useState({ functions: [], methods: { ode: [] } });

  const [functionId, setFunctionId] = useState(0);
  const [x0, setX0] = useState("0");
  const [y0, setY0] = useState("1");
  const [xn, setXn] = useState("1");
  const [h, setH] = useState("0.1");
  const [epsilon, setEpsilon] = useState("0.001");

  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const selectedFunction = useMemo(() => {
    if (!samples.functions || samples.functions.length === 0) return "";
    return samples.functions[functionId] || "";
  }, [samples.functions, functionId]);

  useEffect(() => {
    let alive = true;
    getSamples("ode")
      .then((data) => {
        if (alive) setSamples(data);
      })
      .catch((e) => setError("Ошибка загрузки сэмплов: " + e.message));
    return () => { alive = false; };
  }, []);

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (event) => {
      const content = event.target.result;
      const params = content.trim().split(/[\s,;]+/).map(v => v.replace(",", "."));

      if (params.length >= 6) {
        setFunctionId(parseInt(params[0]));
        setX0(params[1]);
        setY0(params[2]);
        setXn(params[3]);
        setH(params[4]);
        setEpsilon(params[5]);
        setResult(null);
      } else {
        alert("Некорректный формат файла. Нужно: ID_уравнения x0 y0 xn h epsilon");
      }
      e.target.value = "";
    };
    reader.readAsText(file);
  };

  async function handleSolve(e) {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const payload = {
        functionId: Number(functionId),
        x0: parseX(x0),
        y0: parseX(y0),
        xn: parseX(xn),
        h: parseX(h),
        epsilon: parseX(epsilon)
      };

      const res = await solveOde(payload);
      setResult(res);

      if (!res.success) {
        setError(res.globalMessage || "Ошибка при расчете");
      }
    } catch (err) {
      setError(err.message);
      setResult(null);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: "20px 40px", fontFamily: "sans-serif" }}>
      <h2 style={{ borderBottom: "2px solid #6f42c1", paddingBottom: "10px" }}>
        Численное решение ОДУ (Эйлер, улучшенный Эйлер, Милн)
      </h2>

      <div style={{ display: "flex", gap: "30px", alignItems: "flex-start", flexWrap: "wrap" }}>
        <div style={{ flex: "1 1 500px" }}>
          <OdeForm
            samples={samples}
            functionId={functionId}
            setFunctionId={setFunctionId}
            x0={x0} setX0={setX0}
            y0={y0} setY0={setY0}
            xn={xn} setXn={setXn}
            h={h} setH={setH}
            epsilon={epsilon} setEpsilon={setEpsilon}
            onSubmit={handleSolve}
            onFileUpload={handleFileUpload}
            loading={loading}
          />
        </div>

        <div style={{ flex: "1 1 500px" }}>
          <OdeResultPanel result={result} error={error} />
        </div>
      </div>

      <OdeGraph
        originalFormula={selectedFunction}
        result={result}
      />
    </div>
  );
}
